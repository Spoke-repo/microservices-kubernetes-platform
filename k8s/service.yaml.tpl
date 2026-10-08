apiVersion: apps/v1
kind: Deployment
metadata:
  name: ${SERVICE}
  namespace: microservices
  labels:
    app: ${SERVICE}
spec:
  replicas: 2
  selector:
    matchLabels:
      app: ${SERVICE}
  template:
    metadata:
      labels:
        app: ${SERVICE}
    spec:
      containers:
        - name: ${SERVICE}
          image: ${REGISTRY}/${SERVICE}:${TAG}
          ports:
            - containerPort: 8080
          env:
            - name: PORT
              value: "8080"
            - name: JAVA_TOOL_OPTIONS
              value: "-XX:MaxRAMPercentage=75"
            - name: DB_URL
              valueFrom:
                secretKeyRef: { name: db-credentials, key: DB_URL }
            - name: DB_USER
              valueFrom:
                secretKeyRef: { name: db-credentials, key: DB_USER }
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef: { name: db-credentials, key: DB_PASSWORD }
          readinessProbe:
            httpGet: { path: /actuator/health, port: 8080 }
            initialDelaySeconds: 40
            periodSeconds: 10
          livenessProbe:
            httpGet: { path: /actuator/health, port: 8080 }
            initialDelaySeconds: 90
            periodSeconds: 20
          resources:
            requests: { cpu: 100m, memory: 384Mi }
            limits: { cpu: 500m, memory: 768Mi }
---
apiVersion: v1
kind: Service
metadata:
  name: ${SERVICE}
  namespace: microservices
spec:
  type: ClusterIP
  selector:
    app: ${SERVICE}
  ports:
    - port: 80
      targetPort: 8080
