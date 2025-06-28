#!/bin/bash

# Paths to API key files
JACKETT_KEY_FILE="./jackett-api-key.txt"
RADARR_KEY_FILE="./radarr-api-key.txt"

# Read keys from files
JACKETT_API_KEY=$(<"$JACKETT_KEY_FILE")
RADARR_API_KEY=$(<"$RADARR_KEY_FILE")

# Hosts
JACKETT_HOST="http://jackett:9117"
RADARR_HOST="http://localhost:7878"

# Jackett indexer config directory
INDEXER_DIR="./config/Jackett/Indexers"

for file in "$INDEXER_DIR"/*.json; do
    # Skip .bak or .tmp
    [[ "$file" == *.bak || "$file" == *.tmp ]] && continue

    INDEXER_NAME=$(basename "$file" .json)

    # Build Torznab URL
    TORZNAB_URL="${JACKETT_HOST}/api/v2.0/indexers/${INDEXER_NAME}/results/torznab"

    echo "Adding $INDEXER_NAME to Radarr..."

    curl -s -X POST "${RADARR_HOST}/api/v3/indexer" \
      -H "X-Api-Key: ${RADARR_API_KEY}" \
      -H "Content-Type: application/json" \
      -d "{
        \"enableRss\": true,
        \"enableAutomaticSearch\": true,
        \"enableInteractiveSearch\": true,
        \"priority\": 25,
        \"name\": \"${INDEXER_NAME}\",
        \"fields\": [
          {\"name\": \"baseUrl\", \"value\": \"${TORZNAB_URL}\"},
          {\"name\": \"apiKey\", \"value\": \"${JACKETT_API_KEY}\"},
          {\"name\": \"categories\", \"value\": [2000, 5000]}
        ],
        \"implementation\": \"Torznab\",
        \"implementationName\": \"Torznab\",
        \"configContract\": \"TorznabSettings\",
        \"protocol\": \"torrent\"
      }"
done

