NETWORK_NAME := vazzar-app-network
USER_ID := $(shell id -u)
GROUP_ID := $(shell id -g)
RADARR_API_KEY = $(shell cat radarr-api-key.txt)
QBITTORRENT_PASSWORD = $(shell cat qbittorrent-password.txt)

start: create-volumes create-network start-torrent start-radarr start-jackett set-credentials configure-radarr start-radarr-backend

clean:
	- podman stop radarr qbittorrent radarr-backend jackett || true
	- podman rm -f radarr qbittorrent radarr-backend jackett || true
	- podman volume rm qbittorrent-config downloads radarr-config jackett-config movies || true
	- podman network rm $(NETWORK_NAME) || true

create-volumes:
	- podman volume create qbittorrent-config 
	- podman volume create downloads
	- podman volume create radarr-config
	- podman volume create jackett-config
	- podman volume create movies

create-network:
	- podman network create $(NETWORK_NAME)

start-torrent:
	podman run -d --replace --name=qbittorrent \
		--network $(NETWORK_NAME) \
		-p 8081:8081 \
		-e PUID=1000 \
		-e PGID=1000 \
		-e TZ=Etc/UTC \
		-e WEBUI_PORT=8081 \
		-e TORRENTING_PORT=6681 \
		-v qbittorrent-config:/config:rw \
		-v downloads:/downloads \
		docker.io/linuxserver/qbittorrent:latest

start-radarr:
	podman run -d --replace --name=radarr \
		--network $(NETWORK_NAME) \
		-p 7878:7878 \
		-v radarr-config:/config \
		-v downloads:/downloads:rw \
		-v movies:/movies:rw \
		-e PUID=1000 \
		-e PGID=1000 \
		-e TZ=UTC \
		docker.io/linuxserver/radarr:latest

start-jackett:
	podman run -d --replace --name=jackett \
		--network $(NETWORK_NAME) \
		-p 9117:9117 \
		-e PUID=$(USER_ID) \
		-e PGID=$(GROUP_ID) \
		-e TZ=UTC \
		-v jackett-config:/config:rw \
		-v downloads:/downloads \
		docker.io/linuxserver/jackett:latest

start-radarr-backend: build-radarr-backend
	podman run -d --replace --name=radarr-backend \
		--network $(NETWORK_NAME) \
		-p 8080:8080 \
		-e X_API_KEY=$$(cat radarr-api-key.txt) \
		-e QBITTORRENT_PASSWORD=$$(cat qbittorrent-password.txt) \
		-e QBITTORRENT_USERNAME=admin \
		-e RADARR_HOST=radarr \
		-e RADARR_PORT=7878 \
		-e QBITTORRENT_HOST=qbittorrent \
		-e QBITTORRENT_PORT=8081 \
		-e JACKETT_API_KEY=$$(cat jackett-api-key.txt) \
		-e JACKETT_HOST=jackett \
		-e JACKETT_PORT=9117 \
		localhost/radarr-backend:latest

build-radarr-backend:
	podman build -t radarr-backend:latest ./radarr-backend/


configure-radarr:
	@echo "Configuring Radarr Remote Path Mapping..."
	@curl -s -X POST http://localhost:7878/api/v3/remotepathmapping \
		-H "X-Api-Key: $(RADARR_API_KEY)" \
		-H "Content-Type: application/json" \
		-d '{ "host": "localhost", "remotePath": "/downloads", "localPath": "/downloads" }' \
	| jq -r '.message // .error // "Remote Path Mapping done"'

	@echo "Configuring Radarr Root Folder..."
	@curl -s -X POST http://localhost:7878/api/v3/rootfolder \
		-H "X-Api-Key: $(RADARR_API_KEY)" \
		-H "Content-Type: application/json" \
		-d '{"path": "/movies"}' \
	| jq -r 'if type=="array" then "Root folder(s) present or added" else .message // .error // "Root folder added" end'

	@echo "Configuring Radarr Download Client (qBittorrent)..."
	@curl -s -X POST http://localhost:7878/api/v3/downloadclient \
		-H "X-Api-Key: $(RADARR_API_KEY)" \
		-H "Content-Type: application/json" \
		-d '{"enable":true,"priority":1,"name":"qBittorrent","protocol":"torrent","implementation":"qBittorrent","configContract":"qBittorrentSettings","fields":[{"name":"host","value":"qbittorrent"},{"name":"port","value":8081},{"name":"username","value":"admin"},{"name":"password","value":"$(QBITTORRENT_PASSWORD)"},{"name":"category","value":"radarr"}]}' \
	| jq -r 'if type=="array" then "Download client(s) present or updated" else .message // .error // "Download client configured" end'

check-radarr-config:
	@echo "Remote Path Mappings:"
	@curl -s -H "X-Api-Key: $(RADARR_API_KEY)" http://localhost:7878/api/v3/remotepathmapping | jq
	@echo "\nRoot Folders:"
	@curl -s -H "X-Api-Key: $(RADARR_API_KEY)" http://localhost:7878/api/v3/rootfolder | jq
	@echo "\nDownload Clients:"
	@curl -s -H "X-Api-Key: $(RADARR_API_KEY)" http://localhost:7878/api/v3/downloadclient | jq

set-credentials:
	@echo "Fetching qBittorrent temporary password..."
	@sleep 2
	@podman logs qbittorrent 2>&1 | grep 'temporary password is provided for this session:' | sed -E 's/.*session: (.*)/\1/' | tee qbittorrent-password.txt

	@echo "Fetching Radarr API Key..."
	@podman exec -it radarr grep ApiKey /config/config.xml | sed -E 's/.*<ApiKey>([^<]+)<\/ApiKey>.*/\1/' | tee radarr-api-key.txt

	@echo "Triggering Jackett Web UI once to create config..."
	@curl -s --max-time 5 http://localhost:9117/ > /dev/null || true
	@sleep 1

	@echo "Fetching Jackett API Key..."
	@podman exec -it jackett cat /config/Jackett/ServerConfig.json \
		| grep -Po '"APIKey"\s*:\s*"\K[^"]+' \
		| tee jackett-api-key.txt

