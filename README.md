# PoC Arquitectura Microservicios - Modelo Ordenes

# Introducción 
Este proyecto fue creado como Proof of concept para la migración a microservicios del modelo Orders en Poket

# Autor 
Autor: Cristian Daniel Arenas - Development Manager en Poket LLC

El objetivo de esta PoC es separar de la actual arquitectura monolítica el modelo involucrado en el procesamiento de órdenes de compra/venta de activos bursátiles. Esta PoC propone desacoplar parte del modelo de negocio, permitiendo que la API de Poket sea totalmente agnóstica a cuestiones relacionadas con el procesamiento y ejecución de órdenes y la integración con Interactive Brokers.

# Beneficios 
1.  Escalabilidad
2.  Mejor tolerancia a fallos
3.  Tracing distribuido y completo de todos los flujos de comunicación
4.  Servicios Reactivos
5.  Mantenimiento y despliegue independientes
6.  Simplicidad en el manteniemiento y extensión del sistema



![Alt text](https://github.com/cdarenas/microservices_poket_orders/blob/main/Poket%20IB%20Microservices.drawio.png?raw=true "Microservicios - Modelo Ordenes")



# Tecnologías utilizadas
1. Java
2. Spring Boot
3. Spring Cloud
4. Eureka Netflix
5. Spring Cloud Gateway
6. ELK Stack
7. MongoDb
8. MySQL
9. Spring Security OAuth
10. RabbitMQ
11. Docker
12. IB Gateway de Interactive Brokers

# Como correr el Landscape de Microservicios en tu ambiente local con Docker
1. ./gradlew build -x test  && docker-compose build
2. docker-compose up -d
3. Get Authentication Token: curl -k http://writer:secret@localhost:8443/oauth2/token -d grant_type=client_credentials -s | jq .

# Pasos siguientes
Una vez que el landscape de microservicios está corriendo, podemos obtener un Bearer Token par poder autenticarse.

# URLs importantes para acceder localmente
1.  Netflix Service Discovery: http://localhost:8443/eureka/web
2.  Swagger http://localhost:8443/openapi/webjars/swagger-ui/index.html
3.  RabbitMQ http://localhost:15672/#/

# Pendientes
1. Implementar SSL
2. Implementar Patrón de Arquitectura CQRS con MediatoR




