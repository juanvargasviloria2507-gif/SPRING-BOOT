 # Eventify

API REST para gestionar eventos y los lugares donde se realizan. El proyecto ofrece operaciones CRUD, búsquedas básicas y una documentación interactiva mediante OpenAPI/Swagger.

**Autor:** Juan Vargas

## Descripción del proyecto

Eventify centraliza la administración de un catálogo de eventos y lugares. Permite registrar la información principal de cada entidad, consultarla, actualizarla y eliminarla desde una API HTTP.

Actualmente la aplicación utiliza almacenamiento en memoria, por lo que los datos se reinician cada vez que se detiene o vuelve a iniciar el servidor. Al arrancar, se cargan automáticamente tres eventos y tres lugares de ejemplo.

## Problemas que resuelve

- Evita llevar el control de eventos y lugares en archivos o registros manuales.
- Permite consultar rápidamente los eventos programados para una fecha.
- Permite localizar lugares por dirección.
- Centraliza las reglas de validación para nombres, fechas, descripciones, direcciones e identificadores.
- Evita duplicar lugares por nombre y eventos con el mismo nombre y fecha.
- Expone una API documentada que puede consumirse desde Postman, Swagger UI u otra aplicación cliente.

## Tecnologías utilizadas

- Java 17.
- Spring Boot 4.1.1.
- Spring Web MVC para la creación de la API REST.
- Maven y Maven Wrapper para la gestión del proyecto y sus dependencias.
- Lombok para generar constructores, getters y setters.
- SpringDoc OpenAPI 3.1.0 para la documentación y prueba de endpoints.
- JUnit 5, Mockito y Spring Boot Test para las pruebas.
- Colecciones en memoria (`ArrayList`) y `AtomicLong` para los repositorios actuales.

## Arquitectura

Eventify utiliza una arquitectura basada en **MVC (Model-View-Controller)**, adaptada a una API REST y organizada en capas. La aplicación no tiene vistas HTML tradicionales; sus respuestas JSON funcionan como representación para los clientes HTTP.

```text
Cliente HTTP
	|
	v
Controller  ->  Service  ->  Repository  ->  Model
	|
	v
Respuesta JSON
```

- **Model:** `EventsModel` y `VenueModel` representan las entidades y sus datos.
- **View:** la representación se entrega mediante respuestas JSON de la API REST.
- **Controller:** `EventsController` y `VenueController` reciben las peticiones HTTP y devuelven las respuestas.
- **Service:** `EventsService` y `VenueService` contienen las reglas de negocio, validaciones y operaciones CRUD.
- **Repository:** `EventsRepository` y `VenueRepository` gestionan la consulta y persistencia temporal en memoria.
- **Exception handler:** `GloblalExceptionHandler` centraliza el manejo de `EmptyException` y `DuplicateException`.

El flujo general de una petición es:

1. El cliente envía una petición HTTP a un `Controller`.
2. El `Controller` delega la operación al `Service` correspondiente.
3. El `Service` valida los datos y aplica las reglas de negocio.
4. El `Repository` consulta o modifica los datos almacenados en memoria.
5. El `Controller` devuelve una respuesta JSON con el código HTTP correspondiente.

## Requisitos previos

Antes de instalar el proyecto necesitas:

1. Git instalado y disponible en la terminal.
2. JDK 17 instalado y configurado en `JAVA_HOME`.
3. Una terminal compatible con el sistema operativo.
4. Opcionalmente, Postman o un navegador para probar la API y Swagger UI.

No es necesario instalar una base de datos para ejecutar la versión actual.

## Clonar el proyecto

El repositorio remoto de Eventify es:

```text
https://github.com/juanvargasviloria2507-gif/SPRING-BOOT.git
```

Sigue estos pasos para clonarlo correctamente:

1. Instala Git desde [git-scm.com](https://git-scm.com/downloads) si todavía no está instalado.
2. Abre PowerShell, Git Bash o una terminal.
3. Comprueba que Git esté disponible:

```bash
git --version
```

4. Ve a la carpeta donde quieres guardar el proyecto. Por ejemplo:

En Windows:

```powershell
cd C:\Users\<TU_USUARIO>\IdeaProjects
```

En Linux o macOS:

```bash
cd ~/IdeaProjects
```

5. Clona el repositorio y asigna `Eventify` como nombre de la carpeta local:

```bash
git clone https://github.com/juanvargasviloria2507-gif/SPRING-BOOT.git Eventify
cd Eventify
```

6. Comprueba que el repositorio se descargó y que estás en la raíz correcta:

```bash
git status
git remote -v
```

En Windows, lista los archivos con:

```powershell
Get-ChildItem
```

En Linux o macOS, utiliza `ls`:

```bash
ls
```

La raíz debe contener `pom.xml`, `mvnw`, `mvnw.cmd`, `src` y `README.md`. `git remote -v` debe mostrar como origen la URL `https://github.com/juanvargasviloria2507-gif/SPRING-BOOT.git`.

Si la carpeta `Eventify` ya existe y contiene archivos, no ejecutes el comando de clonación dentro de ella. Clona el repositorio en otra carpeta o utiliza una carpeta vacía para evitar conflictos.

## Instalación paso a paso

### Windows

Desde PowerShell, ubicado en la raíz de `Eventify`, ejecuta:

```powershell
java -version
.\mvnw.cmd clean install
```

Si el comando anterior muestra un error relacionado con Java, instala un JDK 17, configura `JAVA_HOME` y vuelve a abrir la terminal. El Maven Wrapper descargará la versión de Maven necesaria para el proyecto, por lo que no necesitas instalar Maven de forma global.

### Linux o macOS

Desde la raíz del proyecto ejecuta:

```bash
java -version
chmod +x mvnw
./mvnw clean install
```

El comando `clean install` descarga las dependencias, compila el código y ejecuta las pruebas.

## Ejecutar el proyecto

### Con Maven Wrapper

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux o macOS:

```bash
./mvnw spring-boot:run
```

La API estará disponible por defecto en:

```text
http://localhost:8080
```

También puedes compilar y ejecutar el JAR generado:

```bash
./mvnw clean package
java -jar target/eventify-0.0.1-SNAPSHOT.jar
```

En Windows, usa `mvnw.cmd` en lugar de `./mvnw`.

## Documentación Swagger

Con la aplicación ejecutándose, abre:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Especificación OpenAPI: `http://localhost:8080/v3/api-docs`

Swagger UI permite revisar los contratos y ejecutar las peticiones directamente desde el navegador.

## Métodos HTTP manejados

La API maneja los siguientes métodos:

- `POST`: crea un evento o un lugar.
- `GET`: consulta todos los registros o filtra por fecha/dirección.
- `PUT`: actualiza un registro existente completo.
- `DELETE`: elimina un registro por su identificador.

## Entidades

### Evento (`EventsModel`)

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | Identificador único. Si se omite al crear, se genera automáticamente. |
| `nombre` | `String` | Nombre del evento. Obligatorio. |
| `fecha` | `LocalDate` | Fecha del evento con formato `YYYY-MM-DD`. Obligatoria. |
| `descripcion` | `String` | Descripción del evento. Obligatoria. |

### Lugar (`VenueModel`)

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | Identificador único. Si se omite al crear, se genera automáticamente. |
| `nombre` | `String` | Nombre del lugar. Obligatorio y no repetido. |
| `direccion` | `String` | Dirección o ciudad del lugar. Obligatoria. |
| `capacidad` | `Long` | Capacidad máxima del lugar. Obligatoria. |

## Endpoints y operaciones CRUD

### Eventos

| Operación | Método y ruta | Código exitoso | Descripción |
|---|---|---:|---|
| Crear | `POST /api/events` | `201 Created` | Registra un evento. |
| Consultar todos | `GET /api/events` | `200 OK` | Devuelve todos los eventos. |
| Buscar por fecha | `GET /api/events/buscar?fecha=2026-10-15` | `200 OK` | Filtra eventos por fecha. |
| Actualizar | `PUT /api/events` | `200 OK` | Actualiza el evento indicado por `id`. |
| Eliminar | `DELETE /api/events/{id}` | `204 No Content` | Elimina el evento indicado por `id`. |

Ejemplo para crear un evento:

```bash
curl -X POST http://localhost:8080/api/events \
	-H "Content-Type: application/json" \
	-d '{"nombre":"Conferencia Java","fecha":"2026-12-10","descripcion":"Conferencia sobre Java y Spring Boot"}'
```

### Lugares

> La ruta implementada usa `Venue` con mayúscula: `/api/Venue`.

| Operación | Método y ruta | Código exitoso | Descripción |
|---|---|---:|---|
| Crear | `POST /api/Venue/create` | `201 Created` | Registra un lugar. |
| Consultar todos | `GET /api/Venue` | `200 OK` | Devuelve todos los lugares. |
| Buscar por dirección | `GET /api/Venue/buscar?direccion=Barranquilla` | `200 OK` | Filtra lugares por dirección exacta. |
| Actualizar | `PUT /api/Venue` | `200 OK` | Actualiza el lugar indicado por `id`. |
| Eliminar | `DELETE /api/Venue/{id}` | `204 No Content` | Elimina el lugar indicado por `id`. |

Ejemplo para crear un lugar:

```bash
curl -X POST http://localhost:8080/api/Venue/create \
	-H "Content-Type: application/json" \
	-d '{"nombre":"Auditorio Central","direccion":"Barranquilla","capacidad":800}'
```

## Cómo utilizar correctamente la API

1. Inicia la aplicación y espera el mensaje de Spring Boot que confirma que el servidor está escuchando en el puerto `8080`.
2. Consulta `GET /api/events` o `GET /api/Venue` para revisar los datos iniciales.
3. Para crear registros, envía JSON con `Content-Type: application/json` y no incluyas un `id` salvo que necesites asignarlo manualmente.
4. Para actualizar, incluye un `id` existente y todos los campos obligatorios en el cuerpo de la petición.
5. Usa fechas con formato ISO `YYYY-MM-DD`, por ejemplo `2026-10-15`.
6. Usa el identificador correcto en la URL al eliminar un registro.
7. Si vuelves a iniciar la aplicación, los datos regresarán a los valores precargados porque el almacenamiento es temporal y está en memoria.

Ejemplo para actualizar un evento:

```bash
curl -X PUT http://localhost:8080/api/events \
	-H "Content-Type: application/json" \
	-d '{"id":1,"nombre":"Conferencia Spring Boot actualizada","fecha":"2026-10-20","descripcion":"Contenido actualizado"}'
```

## Validaciones y excepciones

La capa de servicio valida los datos antes de guardarlos o modificarlos:

- `EmptyException`: se lanza cuando falta un dato obligatorio, el `id` no existe, el `id` es inválido al crear o se intenta eliminar un registro inexistente.
- `DuplicateException`: se lanza cuando se repite un identificador, el nombre de un lugar o la combinación de nombre y fecha de un evento.

`GloblalExceptionHandler` captura ambas excepciones mediante `@RestControllerAdvice` y responde con:

- HTTP `400 Bad Request`.
- Un cuerpo de respuesta con el mensaje descriptivo de la validación.

Las operaciones exitosas responden con `201` al crear, `200` al consultar o actualizar y `204` al eliminar.

## Pruebas

Ejecuta todas las pruebas con:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

Las pruebas cubren el contexto de Spring Boot y las reglas principales de los servicios de eventos y lugares, incluyendo creación, actualización, eliminación, duplicados y datos obligatorios.

## Árbol de archivos

```text
Eventify/
├── .github/
├── .idea/
├── .mvn/
│   └── wrapper/
├── src/
│   ├── main/
│   │   ├── java/com/eventify/
│   │   │   ├── config/
│   │   │   │   ├── DataSeedersConfig.java
│   │   │   │   └── OpenApiConfig.java
│   │   │   ├── controller/
│   │   │   │   ├── EventsController.java
│   │   │   │   └── VenueController.java
│   │   │   ├── exception/
│   │   │   │   ├── DuplicateException.java
│   │   │   │   ├── EmptyException.java
│   │   │   │   └── GloblalExceptionHandler.java
│   │   │   ├── model/
│   │   │   │   ├── EventsModel.java
│   │   │   │   └── VenueModel.java
│   │   │   ├── repository/
│   │   │   │   ├── EventsRepository.java
│   │   │   │   └── VenueRepository.java
│   │   │   ├── service/
│   │   │   │   ├── EventsService.java
│   │   │   │   └── VenueService.java
│   │   │   └── EventifyApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/java/com/eventify/
│       ├── service/
│       │   ├── EventsServiceTest.java
│       │   └── VenueServiceTets.java
│       └── EventifyApplicationTests.java
├── .gitignore
├── HELP.md
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Limitaciones actuales

- Los datos se guardan únicamente en memoria y no persisten entre ejecuciones.
- No hay autenticación ni autorización configuradas.
- La aplicación no define un puerto personalizado; utiliza el puerto `8080` por defecto.
- La búsqueda por dirección compara el valor recibido de forma exacta.
