NETWORK_NAME := vazzar-app-network
USER_ID := $(shell id -u)
GROUP_ID := $(shell id -g)

start: create-volumes create-network start-torrent start-radarr set-credentials

clean:
	- podman stop radarr qbittorrent || true
	- podman rm -f radarr qbittorrent || true

create-volumes:
	- podman volume create qbittorrent-config 
	- podman volume create downloads
	- podman volume create radarr-config
	- podman volume create movies

create-network:
	podman network create $(NETWORK_NAME)

start-torrent:
	podman run -d --replace --name=qbittorrent \
		--network $(NETWORK_NAME) \
		--user $(USER_ID):$(GROUP_ID) \
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
		--user $(USER_ID):$(GROUP_ID) \
		-p 7878:7878 \
		-v radarr-config:/config \
		-v downloads:/downloads:rw \
		-v movies:/movies:rw \
		-e PUID=1000 \
		-e PGID=1000 \
		-e TZ=UTC \
		docker.io/linuxserver/radarr:latest

set-credentials:
	@echo "Fetching qBittorrent temporary password..."
	@podman logs qbittorrent 2>&1 | grep 'temporary password is provided for this session:' | sed -E 's/.*session: (.*)/\1/' | tee qbittorrent-password.txt

	@echo "Fetching Radarr API Key..."
	@podman exec -it radarr grep ApiKey /config/config.xml | sed -E 's/.*<ApiKey>([^<]+)<\/ApiKey>.*/\1/' | tee radarr-api-key.txt

