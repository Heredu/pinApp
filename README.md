# pinApp

![Java](https://img.shields.io/badge/Java-21%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green)
![H2](https://img.shields.io/badge/DataBase-H2-orange)
![JWT](https://img.shields.io/badge/Security-JWT-red)
![Swagger](https://img.shields.io/badge/Docs-Swagger-lightblue)

## 📖 Descripción
API REST desarrollada con Spring Boot para autenticación JWT y gestión de clientes. Incluye sistema completo de registro, autenticación, gestión de usuarios y métricas.

## ✨ Características

- ✅ Autenticación JWT segura
- ✅ Documentación interactiva con Swagger
- ✅ Base de datos H2 embebida (para desarrollo)
- ✅ Validación de datos
- ✅ Manejo de errores 
- ✅ Configuración mediante variables de entorno
- ✅ Dockerización para despliegue fácil
- ✅ Mensajería con Redis para notificar alta de nuevos usuarios



# 📚 Endpoints

## 🔐 Autenticación

Login
`
POST /auth/login
`
```json lines
{
  "username": "admin",
  "password": "admin123"
}
```

Credenciales preconfiguradas:

| Entorno    | Usuario  | Contraseña  |
|------------|----------|-------------|
| Local      | `admin`  | `admin123`  |
| Producción | `pinApp` | `pinApp123` |

```
💡 En el entorno de producción, el servicio puede experimentar una breve demora inicial (aproximadamente 2-3
minutos) durante el primer acceso mientras se completa el despliegue automático.
```
El sistema responderá con un token:

```json lines
{
  "token": "eyJhbGciOi........"
}
```
Header para las peticiones que requieren autenticación:
```http
Authorization: Bearer {token}
```
## 📊 Usuarios 
Crear
`
POST /clients
`

```json lines
{
    "firstName": "Juan",
    "lastName": "Pérez",
    "age": 32,
    "birthDate": "1993-05-15"
}
```
### Obtener Clientes (GET)
Obtener todos los clientes`
GET /clients
`
Requiere autenticación.

### Obtener Metricas (GET)
Crear
`
GET /clients/metrics
`
Requiere autenticación.

### 🌐 Enlaces importantes


| Recurso    | Link                                                         |
|------------|--------------------------------------------------------------|
| 🔗 Producción      | https://mi-app-springboot.onrender.com                       |
| 📚 Swagger UI | https://mi-app-springboot.onrender.com/swagger-ui/index.html |
| 💾 Repositorio     | https://github.com/Heredu/pinApp                             |
| 🚀 Postman | [Colección completa](https://drive.google.com/drive/folders/1hRzYdQhdP0TjnvlmEsprzDUCpkpTDw08)                                       |

### 🛠 Configuración local

- Clonar repo:`git clone https://github.com/Heredu/pinApp.git `
- Acceder al directorio del proyecto: `cd pinApp`
- Instanciar Redis:
  Con Docker instalado ejecutar el siguiente comando para levantar un contenedor de Redis:
`docker run -d -p 6379:6379 --name redis-container redis`
- Ejecutar la aplicación:
`mvn spring-boot:run`


## 🏗️ Arquitectura y Diseño

### Decisiones Arquitectónicas
- **Arquitectura en capas**: Separación clara entre controladores, servicios y repositorios
- **Base de datos embebida**: Uso de H2 para desarrollo y pruebas
- **API RESTful**: Diseño siguiendo mejores prácticas REST
- **Seguridad por defecto**: Todos los endpoints protegidos excepto login, Swagger y health checks
- **Mensajeria**: Publicador de eventos con Redis para notificaciones de alta de nuevos usuarios permitiendo manejarlo de forma asincronica.

### Patrones de Diseño Implementados
- **Singleton**: Para beans de Spring y configuración
- **DTO**: Patrón Data Transfer Object para transferencia de datos
- **Factory**: Para la generación de tokens JWT