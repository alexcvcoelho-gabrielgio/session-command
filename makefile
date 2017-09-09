IMAGE=registry.gitlab.com/gabrielgio/session-command:0.0.1

all: uberjar
	$(MAKE) docker
	$(MAKE) push

push:
	docker push $(IMAGE)

docker:
	docker build -t $(IMAGE) .

uberjar:
	lein uberjar