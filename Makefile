copy-env:
	cp .env.sample .env

# Setup MySql Database
docker-run:
	@if command -v docker > /dev/null; then \
	    docker-compose -f docker/dev-docker-compose.yaml up -d; \
	else \
		echo "Docker is not installed on your machine. Exiting..."; \
		exit 1; \
	fi

# Down Database
docker-down:
	@if command -v docker > /dev/null; then \
	    docker-compose -f docker/dev-docker-compose.yaml down; \
	else \
		echo "Docker is not installed on your machine. Exiting..."; \
		exit 1; \
	fi

# Setup development environment
setup:
	@echo "--- Setting up docker ---"
	@make docker-run

	@echo "--- Copying env ---"
	@make copy-env

	@echo "\n"
	@echo "Setup complete!"