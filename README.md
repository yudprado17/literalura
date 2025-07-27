# LiterAlura: Desafío de Catálogo de Libros y Autores

Este proyecto es la resolución del "Desafío LiterAlura" de Alura Latam, parte de su formación en desarrollo Java. El objetivo principal es construir un catálogo de libros y autores, interactuando con una API pública (Gutendex) y persistiendo los datos en una base de datos PostgreSQL utilizando Spring Data JPA.

## Descripción del Proyecto

LiterAlura es una aplicación de consola en Java que permite a los usuarios buscar libros a través de la API de Gutendex, guardar la información de esos libros y sus autores asociados en una base de datos local, y luego consultar esa información de diversas maneras.

El desafío se centró en aplicar conceptos clave de Java y Spring Boot, incluyendo:
* Consumo de APIs RESTful.
* Manejo de datos JSON con la librería Jackson.
* Persistencia de datos con Spring Data JPA y PostgreSQL.
* Implementación de "Derived Queries" para consultas avanzadas.
* Interacción con el usuario a través de una interfaz de consola.

## Funcionalidades Implementadas

La aplicación ofrece las siguientes opciones en su menú principal:

1.  **Buscar libro por título:** Permite al usuario ingresar un título de libro. La aplicación consulta la API de Gutendex, recupera el primer resultado y lo guarda junto con su autor en la base de datos si no existen previamente.
2.  **Listar libros registrados:** Muestra un listado de todos los libros que han sido guardados en la base de datos local, ordenados por título.
3.  **Listar autores registrados:** Presenta un listado de todos los autores cuyos libros han sido buscados y guardados, ordenados alfabéticamente por nombre.
4.  **Listar autores vivos en un determinado año:** El usuario ingresa un año, y la aplicación busca y muestra todos los autores registrados que estaban vivos en ese año específico.
5.  **Listar libros por idioma:** El usuario ingresa un código de idioma (ej. "es" para español, "en" para inglés), y la aplicación lista todos los libros registrados en ese idioma.
6.  **Mostrar cantidad de libros por idioma:** El usuario selecciona un idioma (entre opciones predefinidas como español, inglés, francés, portugués), y la aplicación muestra el número total de libros registrados en la base de datos para ese idioma.

## Tecnologías Utilizadas

* **Java 17:** Lenguaje de programación.
* **Spring Boot:** Framework para el desarrollo rápido de aplicaciones Java.
    * `spring-boot-starter-data-jpa`: Para la persistencia de datos con JPA.
    * `spring-boot-starter-web`: Incluye Tomcat y Jackson para el manejo de JSON y consumo web.
* **PostgreSQL:** Base de datos relacional utilizada para almacenar la información de libros y autores.
* **Jackson:** Librería para serialización/deserialización de JSON a objetos Java.
* **HttpClient (Java 11+):** Cliente HTTP nativo para realizar solicitudes a la API.
* **API Gutendex:** API pública utilizada para obtener datos de libros.

