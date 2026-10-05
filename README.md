# ModeliaBackend

API REST de **[Modelia](https://github.com/Mohasb/Modelia)**, la tienda multiplataforma con visor 3D y realidad aumentada de mi proyecto final de **Desarrollo de Aplicaciones Multiplataforma (DAM)**. Desarrollada en solitario. **Nota del proyecto: 9.**

[App (Flutter)](https://github.com/Mohasb/Modelia) · [Demo en vivo de la app](https://mohasb.github.io/modeliaWeb/) · [Ver en mi portfolio](https://mohasb.github.io/#proyecto-dam)

## Tecnologías

| Parte | Tecnología |
|---|---|
| Lenguaje y framework | Java 21, Spring Boot 3.3 |
| Seguridad | Spring Security, JWT con refresh tokens (jjwt) |
| Datos | Spring Data JPA, Hibernate, MariaDB / MySQL |
| Validación | Bean Validation |
| Documentación | OpenAPI con Swagger UI (springdoc) |
| Despliegue | Docker (imagen multi-etapa con Maven y Temurin 21) |

## Qué hace

- **Autenticación:** registro, login, refresh y logout con JWT (`/api/auth`). Las rutas se protegen por rol (cliente o administrador).
- **Catálogo:** productos con su modelo 3D, destacados y categorías (`/api/productos`, `/api/categorias`).
- **Pedidos:** creación y consulta de los pedidos del cliente (`/api/pedidos`).
- **Perfil** del usuario (`/api/usuario`).
- **Administración:** alta, edición y baja de productos y categorías, estado de los pedidos, roles y activación de usuarios (`/api/admin`).

Estructura por capas en `com.mhh.modelia`: `controller`, `service`, `repository`, `entity`, `dto`, `security`, `config` y `exception` (errores devueltos en un formato común).

## Ejecutar en local

Necesita Java 21 y una base de datos MariaDB o MySQL.

1. Crea la base de datos `modelia_db`. Las tablas las genera Hibernate al arrancar.
2. Define las variables de entorno (sin ellas usa valores de desarrollo):

   | Variable | Para qué |
   |---|---|
   | `DATABASE_URL` | URL JDBC, por ejemplo `jdbc:mariadb://localhost:3306/modelia_db` |
   | `DATABASE_USERNAME` / `DATABASE_PASSWORD` | Credenciales de la base de datos |
   | `JWT_SECRET` | Clave para firmar los tokens |

3. Arranca la API:

   ```bash
   ./mvnw spring-boot:run
   ```

4. La documentación interactiva queda en `http://localhost:8080/swagger-ui.html`.

Con Docker:

```bash
docker build -t modelia-backend .
docker run -p 8080:8080 -e DATABASE_URL=... -e DATABASE_USERNAME=... -e DATABASE_PASSWORD=... -e JWT_SECRET=... modelia-backend
```

## Autor

**Muhammad Hicho Haidor**, desarrollador full stack.
[Portfolio](https://mohasb.github.io) · [LinkedIn](https://www.linkedin.com/in/mhichohaidor)
