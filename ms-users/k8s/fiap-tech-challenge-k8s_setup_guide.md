
# **Tech Challenge Setup Guide**

This guide will walk you through the process of setting up a Kubernetes environment on Minikube, deploying a PostgreSQL database, a Spring Boot application, and exposing the app via a NodePort service.

## **Pre-requisites**
Before proceeding, make sure you have the following tools installed and configured on your local machine:

1. **Minikube**: To create and manage a local Kubernetes cluster.
   - Install Minikube: [Minikube Installation Guide](https://minikube.sigs.k8s.io/docs/)

2. **kubectl**: Command-line tool to interact with your Kubernetes cluster.
   - Install kubectl: [kubectl Installation Guide](https://kubernetes.io/docs/tasks/tools/install-kubectl/)

3. **Docker**: For building and pushing Docker images.
   - Install Docker: [Docker Installation Guide](https://docs.docker.com/get-docker/)

4. **PostgreSQL Docker Image**: We will use the official PostgreSQL image.

## **Step 1: Start Minikube**

1. Start Minikube by running:
   ```bash
   minikube start
   ```

2. Ensure your Minikube environment is up and running:
   ```bash
   minikube status
   ```

## **Step 2: Build Docker Images**

1. **Build your Spring Boot application Docker image**. Navigate to your project directory and build the image:

   ```bash
   docker build -t tech-challenge-fiap-01:latest .
   ```

2. **Build PostgreSQL Docker image** if necessary (usually the default PostgreSQL image is fine for local setups). You can pull the official image from Docker Hub:

   ```bash
   docker pull postgres:16.8-alpine
   ```

3. **Use Minikube's Docker environment** to build the image directly in Minikube:
   ```bash
   eval $(minikube -p minikube docker-env)
   ```

   Then, rebuild the Spring Boot app Docker image within the Minikube environment:
   ```bash
   docker build -t tech-challenge-fiap-01:latest .
   ```

## **Step 3: Create Secrets for Database Credentials**

In Kubernetes, you can store sensitive data like usernames and passwords as **Secrets**. We will create a secret for your database credentials.

1. **Create a Secret** (`fiap-db-secret`) for the PostgreSQL database credentials (DB_USER and DB_PASS):

   Create a `db-secret.yaml` file:

   ```yaml
   apiVersion: v1
   kind: Secret
   metadata:
     name: fiap-db-secret
   type: Opaque
   data:
     DB_USER: ZmlhcHVzZXI=   # Base64 encoded 'fiapuser'
     DB_PASS: ZmFpcHBhc3M=   # Base64 encoded 'fiappass'
   ```

   To get the base64 encoding of your credentials, you can use the following commands:

   ```bash
   echo -n "fiapuser" | base64
   echo -n "fiappass" | base64
   ```

2. Apply the secret to Kubernetes:

   ```bash
   kubectl apply -f db-secret.yaml
   ```

## **Step 4: Create Deployments**

### 4.1 **PostgreSQL Deployment**

Create a `db-deployment.yaml` file for PostgreSQL deployment:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
   name: fiap-db-deployment
spec:
   replicas: 1
   selector:
      matchLabels:
         app: fiap-db
   template:
      metadata:
         labels:
            app: fiap-db
      spec:
         containers:
            - name: fiap-db
              image: postgres:16.8-alpine
              env:
                 - name: POSTGRES_DB
                   value: fiapdb
                 - name: POSTGRES_USER
                   valueFrom:
                      secretKeyRef:
                         name: fiap-db-secret
                         key: DB_USER
                 - name: POSTGRES_PASSWORD
                   valueFrom:
                      secretKeyRef:
                         name: fiap-db-secret
                         key: DB_PASS
              ports:
                 - containerPort: 5432
```

Apply the PostgreSQL deployment:

```bash
kubectl apply -f db-deployment.yaml
```

### 4.2 **Spring Boot App Deployment**

Create a `deployment.yaml` file for the Spring Boot app deployment:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
   name: tech-challenge-fiap-01-deployment
spec:
   replicas: 5
   strategy:
      rollingUpdate:
         maxSurge: 2
         maxUnavailable: 2
      type: RollingUpdate
   selector:
      matchLabels:
         app: tech-challenge-fiap-01
   template:
      metadata:
         labels:
            app: tech-challenge-fiap-01
      spec:
         containers:
            - name: tech-challenge-fiap-01
              image: gabrielfiapadj/tech-challenge-fiap-01:latest
              ports:
                 - containerPort: 8080
              env:
                 - name: SPRING_DATASOURCE_URL
                   value: jdbc:postgresql://db:5432/fiapdb
                 - name: SPRING_DATASOURCE_USERNAME
                   valueFrom:
                      secretKeyRef:
                         name: fiap-db-secret
                         key: DB_USER
                 - name: SPRING_DATASOURCE_PASSWORD
                   valueFrom:
                      secretKeyRef:
                         name: fiap-db-secret
                         key: DB_PASS
                 - name: SPRING_JPA_HIBERNATE_DDL_AUTO
                   value: update
                 - name: SERVER_PORT
                   value: "8080"
```

Apply the Spring Boot deployment:

```bash
kubectl apply -f deployment.yaml
```

### 4.3 **Create the PostgreSQL Service**

In Kubernetes, services are used to expose pods to other pods or external clients. We'll create a **Service** for PostgreSQL to make the database accessible to the Spring Boot application.

Create a `db-service.yaml` file for the PostgreSQL service:

```yaml
apiVersion: v1
kind: Service
metadata:
  name: db
spec:
  selector:
    app: fiap-db  # The label selector should match the label defined in the PostgreSQL deployment
  ports:
    - protocol: TCP
      port: 5432       # The port that the database will be accessible on
      targetPort: 5432  # The port exposed by the PostgreSQL container
```

Apply the PostgreSQL service:

```bash
kubectl apply -f db-service.yaml
```

---

## **Step 5: Create the NodePort Service to Expose the App**

Create a `node-port-service.yaml` file to expose your Spring Boot app via a **NodePort**:

```yaml
apiVersion: v1
kind: Service
metadata:
   name: tech-challenge-fiap-01-service
spec:
   type: NodePort
   selector:
      app: tech-challenge-fiap-01
   ports:
      - protocol: TCP
        port: 80
        targetPort: 8080
        nodePort: 30080
```

Apply the NodePort service:

```bash
kubectl apply -f node-port-service.yaml
```

## **Step 6: Access the Spring Boot App**

Once your service is created, you can access your Spring Boot application through Minikube:

1. Get the **Minikube IP**:
   ```bash
   minikube ip
   ```

2. Use this IP along with the NodePort (`30080`) to access the application:
   ```
   http://<minikube-ip>:30080
   ```

---

## **Step 7: Verify Everything is Running**

Verify that all deployments, services, and pods are running correctly:

```bash
kubectl get deployments
kubectl get pods
kubectl get svc
```

---

### **Troubleshooting**

- If your pods are stuck or crashing, check their logs:
  ```bash
  kubectl logs <pod-name>
  ```

- Ensure that the PostgreSQL database is running by checking the logs of the `fiap-db` pod:
  ```bash
  kubectl logs <fiap-db-pod-name>
  ```

---

### **Conclusion**

You have successfully deployed a PostgreSQL database and a Spring Boot application on Kubernetes using Minikube. The app is exposed via a **NodePort** service, and you can access it on your local machine using Minikube's IP and the specified port.

Let me know if you need further assistance! 😊