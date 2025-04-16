# 🐳 Remote Debugging Java App in Docker (IntelliJ Guide)

This guide explains how to set up **remote debugging** in **IntelliJ IDEA** for a Java application running in a **Docker container** using **Docker Compose**.

---

## ✅ Prerequisites

- IntelliJ IDEA (Ultimate or Community)
- Docker and Docker Compose installed and running
- Java application containerized with debug port `5005` exposed
- Docker Compose configured to forward port `5005:5005`

---

## ⚙️ IntelliJ Remote Debug Configuration

### 1. **Open IntelliJ**
Launch IntelliJ and open your Java project.

---

### 2. **Create a Remote Debug Configuration**

1. Go to the top menu:
    - `Run` → `Edit Configurations...`
2. Click the **➕ Add New Configuration** button
3. Choose: **Remote JVM Debug**
4. Configure it as follows:
    - **Name**: `Docker Remote Debug`
    - **Host**: `localhost`
    - **Port**: `5005`
    - **Debugger mode**: `Attach to remote JVM`

---

### 3. **Build and Start Containers**

Make sure your Docker image includes the JDWP (Java Debug Wire Protocol) agent.

In terminal:

```bash
docker-compose build --no-cache
docker-compose up
```

📁 The application should now start inside the container and expose the debug port.

---

### 4. **Attach the Debugger**

Once the container is running:

- Click the green debug icon next to the **`Docker Remote Debug`** configuration
- IntelliJ will attach to the JVM running in your container
- You’ll see "Connected to the target VM, address: 'localhost:5005', transport: 'socket'" in the Debug panel

---

### 5. **Add Breakpoints and Debug**

- Set breakpoints in your Java code
- Trigger code execution (e.g. via Postman or browser)
- IntelliJ will pause at your breakpoints so you can inspect variables, step through logic, etc.

---

## 🛠️ Notes

- If you want the app to **wait** until you attach the debugger, change the Dockerfile JDWP config to use `suspend=y`:

  ```text
  -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5005
  ```

- This is useful if you need to debug app startup.

---

## 🧼 Cleanup

When done debugging:

```bash
docker-compose down
```

Or stop the containers via Docker Desktop / CLI.

