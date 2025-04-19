
# Local Continuous Integration Guide

## Overview
This guide will help you set up and use the local continuous integration (CI) process for your project. The goal of this local CI is to automate the process of building, testing, and deploying the application in a development environment.

### The CI process includes the following steps:
1. **Stopping Services**: Stops any running Docker services to ensure a clean environment.
2. **Building the Docker Image**: Builds the application image, tagging it with a version and `latest`.
3. **Running Maven Tests**: Runs Maven tests in an isolated Docker environment to ensure the application works as expected.
4. **Restarting Services**: Brings the Docker services back up after the build and tests.
5. **Cleaning Up**: Removes dangling Docker images to free up disk space.

## Prerequisites

Before proceeding, make sure you have the following installed on your machine:

- **[Docker](https://docs.docker.com/get-docker/)**: Docker should be installed and running.
- **[Docker Compose](https://docs.docker.com/compose/install/)**: Ensure Docker Compose is installed to manage services.
- **[Maven](https://maven.apache.org/install.html)**: Ensure Maven is installed or configured to run with Docker.
- **[Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git)**: Ensure Git is installed for version control.

## Folder Structure

Here is the assumed project folder structure:

```
/project-root
  ├── Dockerfile                           # Dockerfile for building the application image
  ├── app-build.sh                         # Build script to build Docker images
  ├── local-continuous-integration.sh      # Main CI script
  ├── docker-compose.yml                   # Docker Compose file for managing services
```

## Workflow

### 1. Running the Local CI Script

The main CI script, `local-continuous-integration.sh`, automates the process of building the image, running tests, and restarting services. To run it, use the following command:

```bash
./local-continuous-integration.sh [patch|minor|major]
```

#### Arguments:
- `patch`: Increments the patch version of the image (e.g., `1.0.0` → `1.0.1`).
- `minor`: Increments the minor version (e.g., `1.0.0` → `1.1.0`).
- `major`: Increments the major version (e.g., `1.0.0` → `2.0.0`).

#### Example:

```bash
./local-continuous-integration.sh patch
```

This command will:

1. **Stop Docker services** using `docker compose down`.
2. **Build the Docker image** with the specified version increment (`patch`, `minor`, or `major`).
3. **Run Maven tests** in an isolated Docker container.
4. **Restart Docker services** using `docker compose up -d`.
5. **Clean up dangling Docker images** with `docker image prune -f`.

### 2. Local Continuous Integration Process

#### Stopping Docker Services
The script stops the services managed by Docker Compose by running `docker compose down` to ensure a clean environment before building the new image.

#### Building the Docker Image
The script uses `app-build.sh` to build the Docker image for the application. The image will be tagged with both the new version (e.g., `1.0.1`) and `latest`.

If no previous version exists, the script will start from `0.0.0`.

#### Running Maven Tests
Maven tests are executed within an isolated Docker environment using a Maven Docker image. This ensures consistent test execution regardless of your local Maven setup.

If any test fails, the CI process will halt.

#### Restarting Docker Services
Once the build and tests pass, the script restarts the Docker services using `docker compose up -d` to run the containers in detached mode.

#### Cleaning Up
Once the CI process is complete, the script will clean up any unused Docker images with `docker image prune -f` to free up space.

### 3. Customizing the CI Process

You can modify certain aspects of the CI process:

- **Build Script**: Modify `app-build.sh` to adjust how the image is built (e.g., adding build arguments or environment variables).
- **Versioning**: Adjust versioning logic in `local-continuous-integration.sh` to suit your version control needs.

### 4. Troubleshooting

Here are some common issues and troubleshooting tips:

#### Permission Issues
Ensure all scripts are executable by running:

```bash
chmod +x ./local-continuous-integration.sh
chmod +x ./app-build.sh
```

#### Docker Not Running
Ensure Docker is installed and running. Check with:

```bash
docker --version
```

#### Maven Test Failures
If Maven tests fail, the CI process will stop. You need to fix any failing tests before rerunning the script.

---

## Example CI Workflow

### Incrementing the Patch Version

To increment the patch version of your image:

```bash
./local-continuous-integration.sh patch
```

The script will:

1. **Stop the services** using `docker compose down`.
2. **Build the image** and tag it as `1.0.1` (if the last version was `1.0.0`).
3. **Run tests** in a Docker container using Maven.
4. **Restart the services** and clean up any unused images.

---

## Conclusion

This local CI setup automates the process of building, testing, and deploying your application in a Docker environment. By using Docker and Maven, the process ensures a consistent and isolated testing environment, regardless of your local machine's configuration.
