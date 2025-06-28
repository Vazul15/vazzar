# Build dist folder from source
FROM docker.io/node:18-alpine AS client
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Download and build with local configuration
FROM docker.io/golang:1.24.4-alpine AS builder
WORKDIR /app
RUN apk add git 
RUN git clone https://github.com/mmarci96/reverse-proxy-go.git ./
RUN go mod download
COPY --from=client /app/dist/ /app/resources/static/
COPY ./proxy-config.yaml /app/resources/config.yaml 
RUN CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o main ./cmd/main.go

# Run the built binaries and serve static files
FROM docker.io/alpine:3.17
WORKDIR /app
COPY --from=builder /app/main .
COPY --from=builder  /app/resources ./resources
EXPOSE 8000

ENTRYPOINT ["/app/main"]
