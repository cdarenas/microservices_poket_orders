# microservices_poket_orders

# Introduction 
Este proyecto fue creado como Proof of concept para la migracion a microservicios del modelo Orders en Poket
Autor: Cristian Daniel Arenas - Development Manager

El objetivo de esta PoC es separar de la actual arquitectura monolítica el modelo involucrado en el procesamiento de órdenes de compra/venta de activos bursátiles. Esta PoC propone desacoplar parte del modelo de negocio, permitiendo que la API de Poket sea totalmente agnóstica a cuestiones relacionadas con el procesamiento y ejecución de órdenes y la integración con Interactive Brokers.

Beneficios:

*Escalabilidad
*Mejor tolerancia a fallos
*Tracing distribuido y completo de todos los flujos de comunicación
*Servicios Reactivos
*Mantenimiento y despliegue independientes
*Simplicidad en el manteniemiento y extensión del sistema

![Alt text](https://github.com/cdarenas/microservices_poket_orders/blob/main/Poket%20IB%20Microservices.drawio.png?raw=true "Microservicios - Modelo Ordenes")

# Getting Started
TODO: Guide users through getting your code up and running on their own system. In this section you can talk about:
1.	Installation process
2.	Software dependencies
3.	Latest releases
4.	API references

# Build and Test
TODO: Describe and show how to build your code and run the tests. 

# Contribute
TODO: Explain how other users and developers can contribute to make your code better. 

If you want to learn more about creating good readme files then refer the following [guidelines](https://docs.microsoft.com/en-us/azure/devops/repos/git/create-a-readme?view=azure-devops). You can also seek inspiration from the below readme files:
- [ASP.NET Core](https://github.com/aspnet/Home)
- [Visual Studio Code](https://github.com/Microsoft/vscode)
- [Chakra Core](https://github.com/Microsoft/ChakraCore)
