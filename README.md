Descripción:
    La solución es una API REST desarrollada con Java y Spring Boot, que permite gestionar una biblioteca y mantener un registro de los libros y autores asociados.

Unas apreciaciones iniciales:

    Se ha entendido que los usuarios podían tener mas de un rol.

    Se ha entendido que en caso de intentar crear un autor o libro con un libro o autor que no se hayan creado ya se de un error de que no se pudo encontrar al autor o libro y se pide que se vuelva a intentar enviar la petición, se recomienda que el flujo sea crear el autor, luego el libro y ya sea desde crear el libro o desde el metodo de editar un libro que se le asocie el autor al libro.

Usuarios del sistema:

    Usuarios anónimos: Pueden consultar los libros y los datos de los autores.

    Bibliotecarios: A mayores, pueden registrar y editar libros y autores.

    Directivos: A mayores, pueden exportar un resumen en formato Excel.

Tecnologias y dependencias:

    Java: 17.

    Spring Boot: 3.1.9

    Base de datos: H2 en memoria. se genera de nuevo cada vez que se arranca la aplicación.

    Nimbus JOSE + JWT, librería para la gestion de tokens JWT para gestionar las autorizaciones y los logins. Ahora mismo los tokens estan dispuestos para durar 10 minutos asi que despues de eso 'expira' la sesion.

    Lombok, 1.18.30, librería para agilizar y haceer mas legibles los modelos, dtos y otras clases.

    Springdoc Open AI, version 2.3.0, swagger, para documentar la aplicacion, sobre todo los endpoints, genera una url cuando se despliega la aplicacion en el que se pueden revisar: http://localhost:8080/swagger-ui

    Junit y mockito para los tests unitarios.

    Apache Poi, 5.2.3 para la generacion de ficheros .xlsx para la exportacion que pueden hacer las personas con rol de directivo (llamadas manager en la aplicacion).

Scaffolding:

    El proyecto tiene la siguiente estructura

    /src
        /main
            /java
            /com
                /biblioteca
                /biblioteca_api
                    - BibliotecaApiApplication.java (Clase principal)
                    - config (Configuraciones como swagger o seguridad)
                    - controller (Controladores de la API)
                    - dto (Classes para transferir data a y desde la aplicacion, respuestas de endpoints o cuerpos para requests)
                    - exception (Excepciones personalizadas y el handler global de excepciones)
                    - mapper (Clases mapper, para mapear unos objetos a sus DTOs)
                    - model (Clases de los modelos de datos)
                    - repository (Interfaces de repositorios JPA)
                    - security (Configuracion de los microserevicios de tokens JWT)
                    - service (Lógica de negocio)
                    - util (Cosas como el generador de excell)
            /resources
            - application.properties (Configuración de la base de datos y otros parámetros)
            - data.sql (Data que rellenara la base en memoria de la aplicación)
            - static (Recursos estáticos si son necesarios)
            - templates (Plantillas si las hay)
        /test
            /java
            /com
                /biblioteca
                /biblioteca_api
                    - BibliotecaApiApplicationTest.java (Test de la clase principal)
                    - config (Configuraciones para el entorno de testing)
                    - controller (Tests de los controllers)
                    - exception (Test del handler de excepciones)
                    - service (Test de la capa de servicios)
                    - util (Test de los utils)

Endpoints:

    Junto a este readme se adjuntará una postman collection con la que se podrán probar los diferentes endpoints.

    Como es una stateless RESTful api, el loging funciona devolviendo un token que hay que pegar como bearer token al realizar alguna de las peticiones que requieren autentificacion o un rol en especifico, tambien se puede recuperar ese token como variable de entorno de postman y pasar esa variable a el bearer token. Dicho esto, para el caso especifico del export, postman permite enviar y descargar, pero se suele olvidar de el formato del archivo (.xlsx), haciendo la peticion desde un navegador web iría bien pero requiere un rol de directivo para tener acceso al endpoint. Siento las molestias.

    Get /authors - devuelve una lista con todos los autores
    Get /authors/{id} - devuelve la informacion del autor cuyo id se haya pasado en la ruta
    Post /authors - Crea un autor
    Put /authors/{id} - Edita el autor que se haya pasado en la ruta
    Get /books - devuelve una lista con todos los libros
    Get /books/{id} - devuelve la informacion del libro cuya id se haya pasado en la ruta
    Post /books - Crea un libro
    Put /books/{id} - Edita el libro que se haya pasado en la ruta
    Post /users/register - Registra un nuevo usuario
    Post /auth/login - Inicia la sesion
    Get /export - Exporta un excel con los datos del total de libros, total de autores, total de libros por autor.

Ejecucion del proyecto:

    El proyecto incluye un Dockerfile, simplemente construir la imagen del proyecto usando el comando docker build -t biblioteca-api . desde una terminal al nivel de este documento README.md

    Luego ejecutar el siguiente comando para levantar la imagen:

    docker run -p 8080:8080 -e MAIL_USERNAME=apikey -e MAIL_PASSWORD=SENDGRIND_APIKEY --name biblioteca-api-container biblioteca-api

    Donde SENDGRIND_APIKEY tiene que ser cambiada por el api key que habré mandado por correo junto al proyecto. (debería durar hasta el 8 de julio el servicio de mensajería)

    Y con eso debería ejecutarse el proyecto y con el su base de datos en memoria.

Conclusión:

    Ha sido un proyecto bastante interesante que me ha hecho enfrentarme a partes del desarrollo en springboot con el que aun no estaba muy acostumbrado, destacando especialmente que lo que mas me costó realizar fue la autentificacion, la seguridad en springboot, los tokens y todo lo relacionado con el login y los tests de los controladores.

    Al mismo nivel que este README.md dejo una carpeta respuestas que traen las respuestas a las dos preguntas planteadas al final de la prueba.