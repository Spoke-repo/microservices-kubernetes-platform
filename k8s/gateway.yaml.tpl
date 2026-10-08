apiVersion: apps/v1
kind: Deployment
metadata:
  name: api-gateway
  namespace: microservices
  labels:
    app: api-gateway
spec:
  replicas: 2
  selector:
    matchLabels:
      app: api-gateway
  template:
    metadata:
      labels:
        app: api-gateway
    spec:
      containers:
        - name: api-gateway
          image: ${REGISTRY}/api-gateway:${TAG}
          ports:
            - containerPort: 8080
          env:
            - name: PORT
              value: "8080"
            - name: JAVA_TOOL_OPTIONS
              value: "-XX:MaxRAMPercentage=75"
            - { name: USER_URL, value: "http://user-service" }
            - { name: CUSTOMER_URL, value: "http://customer-service" }
            - { name: PRODUCT_URL, value: "http://product-service" }
            - { name: INVENTORY_URL, value: "http://inventory-service" }
            - { name: ORDER_URL, value: "http://order-service" }
            - { name: PAYMENT_URL, value: "http://payment-service" }
            - { name: NOTIFICATION_URL, value: "http://notification-service" }
            - { name: SHIPPING_URL, value: "http://shipping-service" }
            - { name: REPORT_URL, value: "http://report-service" }
          readinessProbe:
            httpGet: { path: /actuator/health, port: 8080 }
            initialDelaySeconds: 30
            periodSeconds: 10
          livenessProbe:
            httpGet: { path: /actuator/health, port: 8080 }
            initialDelaySeconds: 60
            periodSeconds: 20
          resources:
            requests: { cpu: 100m, memory: 384Mi }
            limits: { cpu: 500m, memory: 768Mi }
---
apiVersion: v1
kind: Service
metadata:
  name: api-gateway
  namespace: microservices
spec:
  type: LoadBalancer
  selector:
    app: api-gateway
  ports:
    - port: 80
      targetPort: 8080
