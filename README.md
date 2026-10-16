# Credifiel

Aplicación CRUD para administrar un directorio de personas. El repositorio concentra el frontend Angular y el servicio REST Spring Boot en proyectos independientes.

## Arquitectura

```text
credifiel/
├── crudFront/  # Cliente web Angular + Tailwind CSS
└── service/    # API REST Spring Boot + SQL Server + Flyway
```

El frontend se comunica con `http://localhost:8080/api/personas`. El servicio habilita CORS para los orígenes de desarrollo `http://localhost:4200` y `http://127.0.0.1:4200`.

## Funcionalidad

- Consultar el listado de personas.
- Ver el detalle de una persona por identificador.
- Registrar personas con nombre y edad.
- Editar registros existentes.
- Eliminar personas mediante confirmación.
- Validar los datos antes de enviarlos y mostrar errores de API en el cliente.

## Tecnologías

| Proyecto | Tecnologías |
| --- | --- |
| `crudFront` | Angular 19, TypeScript, Reactive Forms, Tailwind CSS 4 |
| `service` | Java 17, Spring Boot, Spring Data JPA, Flyway, Gradle, SQL Server |

## API REST

Base URL: `http://localhost:8080/api/personas`

| Método | Endpoint | Descripción | Respuesta exitosa |
| --- | --- | --- | --- |
| `GET` | `/api/personas` | Lista todas las personas. | `200 OK` |
| `GET` | `/api/personas/{id}` | Consulta una persona por ID. | `200 OK` |
| `POST` | `/api/personas` | Crea una persona. | `201 Created` |
| `PUT` | `/api/personas/{id}` | Actualiza una persona. | `200 OK` |
| `DELETE` | `/api/personas/{id}` | Elimina una persona. | `204 No Content` |

### Cuerpo de creación y actualización

```json
{
  "nombre": "Ana López",
  "edad": 30
}
```

`nombre` es obligatorio y admite hasta 100 caracteres. `edad` es obligatoria y debe estar entre 0 y 150.

## Requisitos

- Java 17
- Node.js 20 LTS o superior
- npm
- SQL Server

## Ejecutar localmente

### 1. Configurar y ejecutar el servicio

La base de datos `crud_personas` debe existir. Flyway crea y versiona automáticamente la tabla `personas`.

```bash
cd service
export DB_URL='jdbc:sqlserver://localhost:1433;databaseName=crud_personas;encrypt=true;trustServerCertificate=true'
export DB_USERNAME='sa'
export DB_PASSWORD='contraseña-segura'
./gradlew bootRun
```

El servicio estará disponible en `http://localhost:8080`.

### 2. Ejecutar el frontend

En otra terminal:

```bash
cd crudFront
npm install
npm start
```

Abre `http://localhost:4200` en el navegador. El frontend incluye diseño responsivo con Tailwind CSS y consume la API local.

## Validación

```bash
# Frontend
cd crudFront
npm run build
npm test -- --watch=false --browsers=ChromeHeadless

# Servicio
cd service
./gradlew test
```
