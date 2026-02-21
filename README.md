## Blueprints API – ARSW Lab 4

API REST desarrollada con Spring Boot para la gestión de *blueprints* (planos compuestos por puntos).  
El proyecto implementa arquitectura en capas, persistencia desacoplada y procesamiento configurable mediante perfiles de Spring.

---

## Requisitos
- Java 21
- Maven 3.9+
- PostgreSQL

## Ejecución del proyecto
```bash
mvn clean install
mvn spring-boot:run
```

La aplicación se ejecuta en:
```bash
http://localhost:8080
```

## Activación de filtros (Perfiles)

La aplicación permite cambiar el procesamiento de los blueprints usando perfiles de Spring.

**Sin perfil (comportamiento por defecto)**

Usa IdentityFilter (no modifica los puntos):
```bash
mvn spring-boot:run
```

**Activar RedundancyFilter**

Elimina puntos consecutivos duplicados:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=redundancy
```

**Activar UndersamplingFilter**

Conserva 1 de cada 2 puntos:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=undersampling
```

---

## Pruebas con curl

Obtener todos los blueprints
```bash
curl -s http://localhost:8080/api/v1/blueprints
```

Obtener blueprint específico
```bash
curl -s http://localhost:8080/api/v1/blueprints/john/house
```

Crear blueprint
```bash
curl -i -X POST http://localhost:8080/api/v1/blueprints \
-H "Content-Type: application/json" \
-d '{ "author":"john","name":"kitchen","points":[{"x":1,"y":1},{"x":2,"y":2}] }'
```

---

## Documentación
  
- Swagger UI disponible en:: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)  
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)  

---

## Arquitectura

```
src/main/java/edu/eci/arsw/blueprints
  ├── model/         # Entidades de dominio: Blueprint, Point
  ├── persistence/   # Interfaz + repositorios (InMemory, Postgres)
  │    └── impl/     # Implementaciones concretas
  ├── services/      # Lógica de negocio y orquestación
  ├── filters/       # Filtros de procesamiento (Identity, Redundancy, Undersampling)
  ├── controllers/   # REST Controllers (BlueprintsAPIController)
  └── config/        # Configuración (Swagger/OpenAPI, etc.)
```

Se aplican principios como:

- Separación de responsabilidades

- Programación contra interfaces

- Inyección de dependencias

- Uso de perfiles para comportamiento dinámico
