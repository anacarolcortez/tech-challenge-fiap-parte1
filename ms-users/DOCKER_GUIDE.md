## 🐳 Docker Execution Guide — Versioned Build & Compose Setup

This project uses Docker and Docker Compose with automatic image versioning.

---

### 📁 Docker-Related Files

| File              | Purpose                                                           |
|------------------|-------------------------------------------------------------------|
| `Dockerfile`      | Defines how the application image is built.                      |
| `docker-compose.yml` | Orchestrates services (app + PostgreSQL database).                |
| `build.sh`        | Script to build Docker images using semantic versioning logic.   |

---

### 🧱 How to Build the Image

Use the `build.sh` script to build your image with automatic versioning:

```bash
chmod +x build.sh
./build.sh             # Increments patch (x.y.z → x.y.(z+1)) – default
./build.sh minor       # Increments minor (x.y.z → x.(y+1).0)
./build.sh major       # Increments major (x.y.z → (x+1).0.0)
```

The script will:
- Detect the latest local version of the image (e.g., `tech-challenge-fiap-01:0.0.3`)
- Generate the next version based on the selected level
- Tag the image as both `:<new-version>` and `:latest`

---

### 📦 Example Output

```bash
✅ Successfully built:
   - tech-challenge-fiap-01:0.0.4
   - tech-challenge-fiap-01:latest
```

---

### 🚀 Starting the Containers

Once the image is built, run:

```bash
docker-compose up -d
```

This will:
- Start the application container using the latest versioned image
- Spin up the PostgreSQL container
- Inject environment variables automatically via `docker-compose.yml`
- Connect both containers via an internal Docker network

---

### 🧼 Shutting Down

```bash
docker-compose down
```
