# pinApp
Gestión de clientes

![Java](https://img.shields.io/badge/Java-21%2B-blue)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green)
![H2](https://img.shields.io/badge/DataBase-H2-orange)

API REST desarrollada con Spring Boot para autenticación JWT y gestión de recursos.

## 📋 Tabla de Contenidos
- [Instalación](#-instalación)
- [Autenticación](#-autenticación)
- [Endpoints](#-endpoints)
- [Variables de Entorno](#%EF%B8%8F-variables-de-entorno)
- [Despliegue](#-despliegue-en-render)
- [Solución de Problemas](#-troubleshooting)

## 🚀 Instalación

### Pasos
1. Clonar repositorio:

git clone https://github.com/tu-usuario/mi-app-springboot.git
cd mi-app-springboot

# 📚 Endpoints de Usuarios

## 📊 Tabla de Endpoints

| Método | Endpoint                | Requiere Autenticación | Requiere Body | Descripción         |
|--------|-------------------------|------------------------|---------------|---------------------|
| POST   | `/clients`              | ✅ Sí                  | ✅ Sí         | Crear nuevo usuario |
| POST    | `auth/login`            | ❌ No                 | ✅ Sí          | Login               |
| GET    | `/clients`              | ✅ Sí                  | ❌ No         | Obtener clientes    |
| GET    | `/clients/metrics` | ✅ Sí                  | ❌ No         | Metricas            |
| GET    | `/swagger-ui/index.html` | ❌ No                    | ❌ No         | Swagger             |


## 🛠 Ejemplos de Uso

### Crear Usuario (POST)
```http
POST /api/users HTTP/1.1
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json
Host: api.tudominio.com

{
  "username": "nuevo_user",
  "password": "Pass123!",
  "email": "user@example.com",
  "roles": ["ROLE_USER"]
}