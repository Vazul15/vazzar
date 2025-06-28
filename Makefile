NETWORK_NAME := vazzar-app-network
USER_ID := $(shell id -u)
GROUP_ID := $(shell id -g)
RADARR_API_KEY = $(shell cat radarr-api-key.txt)
QBITTORRENT_PASSWORD = $(shell cat qbittorrent-password.txt)
JACKETT_API_KEY = $(shell cat jackett-api-key.txt)
TMDB_API_KEY := your-api-key

-include .env

start: create-volumes create-network start-torrent start-jackett start-radarr set-credentials  configure-radarr start-tmdb-backend start-radarr-backend 

stop: stop-radarr-backend stop-tmdb-backend stop-frontend
	- podman stop radarr qbittorrent jackett || true

remove: stop remove-radarr-backend remove-tmdb-backend remove-frontend
	- podman rm -f radarr qbittorrent jackett || true

clean: remove
	- podman volume rm qbittorrent-config radarr-config jackett-config || true
	- podman network rm -f $(NETWORK_NAME) || true

create-volumes:
	- podman volume create qbittorrent-config 
	- podman volume create radarr-config
	- podman volume create jackett-config
	- mkdir downloads movies

create-network:
	- podman network create $(NETWORK_NAME)

start-torrent:
	podman run -d --replace --name=qbittorrent \
		--network $(NETWORK_NAME) \
		-p 8081:8081 -p 6881:6881 \
		-e PUID=$(USER_ID) \
		-e PGID=$(GROUP_ID) \
		-e TZ=UTC \
		-e WEBUI_PORT=8081 \
		-e TORRENTING_PORT=6881 \
		-v qbittorrent-config:/config:Z \
		-v $(PWD)/downloads:/downloads:Z \
		docker.io/linuxserver/qbittorrent:latest

start-radarr:
	podman run -d --replace --name=radarr \
		--network $(NETWORK_NAME) \
		-p 7878:7878 \
		-v radarr-config:/config:Z \
		-v $(PWD)/downloads:/downloads:Z \
		-v $(PWD)/movies:/movies:Z \
		-e PUID=$(USER_ID) \
		-e PGID=$(GROUP_ID) \
		-e TZ=UTC \
		docker.io/linuxserver/radarr:latest

start-jackett:
	podman run -d --replace --name=jackett \
		--network $(NETWORK_NAME) \
		-p 9117:9117 \
		-e PUID=$(USER_ID) \
		-e PGID=$(GROUP_ID) \
		-e TZ=UTC \
		-v jackett-config:/config:Z \
		-v $(PWD)/config/Jackett/Indexers:/config/Jackett/Indexers:Z \
		-v $(PWD)/downloads:/downloads \
		docker.io/linuxserver/jackett:latest

start-frontend: build-frontend
	@echo "Starting Vazzar frontend..."
	- podman run -d --replace --name vazzar-frontend \
		--network $(NETWORK_NAME) \
		-p 8000:8000 \
		localhost/vazzar-frontend:latest

build-frontend: 
	@echo "Building Vazzar frontend..."
	- podman build -t vazzar-frontend ./frontend/

stop-frontend: 
	@echo "Stopping Vazzar frontend"
	- podman stop vazzar-frontend

remove-frontend: stop-frontend
	@echo "Removing Vazzar frontend..."
	- podman rm -f vazzar-frontend
	
start-tmdb-backend: build-tmdb-backend
	podman run -d --replace --name tmdb-backend \
		--network $(NETWORK_NAME) \
		-p 8090:8080 \
		-e TMDB_API_KEY=$(TMDB_API_KEY) \
		localhost/tmdb-backend:latest
	
build-tmdb-backend:
	podman build -t tmdb-backend:latest ./tmdb-backend/

stop-tmdb-backend:
	@echo "Stopping Tmdb backend..."
	- podman stop tmdb-backend

remove-tmdb-backend: stop-tmdb-backend
	@echo "Removing TMDB backend..."
	- podman rm -f tmdb-backend
	

start-radarr-backend: build-radarr-backend
	podman run -d --replace --name=radarr-backend \
		--network $(NETWORK_NAME) \
		-p 8080:8080 \
		-e X_API_KEY=$(RADARR_API_KEY) \
		-e RADARR_HOST=radarr \
		-e RADARR_PORT=7878 \
		localhost/radarr-backend:latest

build-radarr-backend:
	podman build -t radarr-backend:latest ./radarr-backend/

stop-radarr-backend:
	@echo "Stopping radarr backend..."
	- podman stop radarr-backend

remove-radarr-backend: stop-radarr-backend
	- podman rm -f radarr-backend

check-radarr-config:
	@echo "Remote Path Mappings:"
	@curl -s -H "X-Api-Key: $(RADARR_API_KEY)" http://localhost:7878/api/v3/remotepathmapping | jq
	@echo "\nRoot Folders:"
	@curl -s -H "X-Api-Key: $(RADARR_API_KEY)" http://localhost:7878/api/v3/rootfolder | jq
	@echo "\nDownload Clients:"
	@curl -s -H "X-Api-Key: $(RADARR_API_KEY)" http://localhost:7878/api/v3/downloadclient | jq
	@echo "\nIndexer:"
	@curl -s -H "X-Api-Key: $(RADARR_API_KEY)" http://localhost:7878/api/v3/indexer | jq
	

set-credentials:
	@echo "Fetching qBittorrent temporary password..."
	@sleep 3
	@podman logs qbittorrent 2>&1 | grep 'temporary password is provided for this session:' | sed -E 's/.*session: (.*)/\1/' | tee qbittorrent-password.txt
	@echo "Fetching Radarr API Key..."
	@podman exec -it radarr grep ApiKey /config/config.xml | sed -E 's/.*<ApiKey>([^<]+)<\/ApiKey>.*/\1/' | tee radarr-api-key.txt
	@echo "Triggering Jackett Web UI once to create config..."
	@curl -s --max-time 5 http://localhost:9117/ > /dev/null || true
	@echo "Fetching Jackett API Key..."
	@podman exec -it jackett cat /config/Jackett/ServerConfig.json \
		| grep -Po '"APIKey"\s*:\s*"\K[^"]+' \
		| tee jackett-api-key.txt

configure-radarr: configure-radarr-mapping configure-radarr-root configure-radarr-downloadclient configure-radarr-jackett
	
configure-radarr-mapping:
	@echo "Configuring Radarr Remote Path Mapping..."
	@sleep 2
	@curl -s -X POST http://localhost:7878/api/v3/remotepathmapping \
		-H "X-Api-Key: $(RADARR_API_KEY)" \
		-H "Content-Type: application/json" \
		-d '{ "host": "localhost", "remotePath": "/downloads", "localPath": "/downloads" }' \
	| jq -r '.message // .error // "Remote Path Mapping done"'

configure-radarr-root:
	@echo "Configuring Radarr Root Folder..."
	@sleep 2
	@curl -s -X POST http://localhost:7878/api/v3/rootfolder \
		-H "X-Api-Key: $(RADARR_API_KEY)" \
		-H "Content-Type: application/json" \
		-d '{"path": "/movies"}' \
	| jq 

configure-radarr-downloadclient:
	@echo "Configuring Radarr Download Client (qBittorrent)..."
	@sleep 2
	@curl -s -X POST http://localhost:7878/api/v3/downloadclient \
		-H "X-Api-Key: $(RADARR_API_KEY)" \
		-H "Content-Type: application/json" \
		-d '{"enable":true,"priority":1,"name":"qBittorrent","protocol":"torrent","implementation":"qBittorrent","configContract":"qBittorrentSettings","fields":[{"name":"host","value":"qbittorrent"},{"name":"port","value":8081},{"name":"username","value":"admin"},{"name":"password","value":"$(QBITTORRENT_PASSWORD)"},{"name":"category","value":"radarr"}]}' \
	| jq -r 'if type=="array" then "Download client(s) present or updated" else .message // .error // "Download client configured" end'

configure-radarr-jackett:
	@echo "Configuring Radarr Indexers (Jackett)..."
	@sleep 2
	@curl -s -X POST http://localhost:7878/api/v3/indexer \
		-H "X-Api-Key: $(RADARR_API_KEY)" \
		-H "Content-Type: application/json" \
		-d '{"enableRss":true,"enableAutomaticSearch":true,"enableInteractiveSearch":true,"priority":10,"name":"ThePirateBay","fields":[{"name":"baseUrl","value":"http://jackett:9117/api/v2.0/indexers/thepiratebay/results/torznab/"},{"name":"apiKey","value":"$(JACKETT_API_KEY)"},{"name":"categories","value":[2000,2020,2040]}],"implementation": "Torznab","configContract":"TorznabSettings","tags":[]}' \
	| jq
	@echo "Configuring Radarr Second try (Jackett)..."
	@sleep 2
	@curl -s -X POST http://localhost:7878/api/v3/indexer \
		-H "X-Api-Key: $(RADARR_API_KEY)" \
		-H "Content-Type: application/json" \
		-d '{"enableRss":true,"enableAutomaticSearch":true,"enableInteractiveSearch":true,"priority":10,"name":"ThePirateBay","fields":[{"name":"baseUrl","value":"http://jackett:9117/api/v2.0/indexers/thepiratebay/results/torznab/"},{"name":"apiKey","value":"$(JACKETT_API_KEY)"},{"name":"categories","value":[2000,2020,2040]}],"implementation": "Torznab","configContract":"TorznabSettings","tags":[]}' \
	| jq


