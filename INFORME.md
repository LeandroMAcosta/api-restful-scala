# API REST

## Indice 

1. Introducción
2. ¿Qué es una API RESTful? 
3. Nuestro Modelo 
4. Dificultades
5. Bibliografía 
6. Integrantes 

## Introducción 

En este laboratorio vamos a implementar una API RESTful para un servicio de delivery, al estilo Rappi, PedidosYA,ifood o Deliveroo. En esta API, los proveedores suben sus productos, que los consumidores pueden ver y seleccionar para incluir en un pedido. Una vez que el pedido está finalizado, la aplicación se encarga de cobrarlo al consumidor, entregarlo y pagarle al proveedor el dinero correspondiente.

1. Encapsulamiento
2. Herencia, clases abstractas y traits.
3. Sobrecarga de operadores
4. Polimorfismo


## ¿Qué es una API REST?
Una API RESTful, también conocida como servicio web RESTful,se basa en la tecnología de transferencia de estado representacional (**REST**). 

REST es cualquier interfaz entre sistemas que use HTTP para obtener datos o generar operaciones sobre esos datos en todos los formatos posibles como por ejemplo XML (en nuestri caso vamos a trabajar con JSON).

Posee muchas ventajas, entre las que se destacan más es que es un protocolo cliente/servidor ``sin estados``, cada petición HTTP contiene toda la información necesaria para ejecutarla, por lo tanto ni el cliente ni el servidor necesitan recordar ningún estado para satisfacer dicha petición.

>Las operaciones más importantes relacionadas con los datos en cualquier sistema REST son: 
>   * POST (crear)
>   * GET (leer y consultar)
>   * PUT (editar)
>   * DELETE (eliminar).

## Nuestro Modelo
![database](diagrams/database.png)

## Dificultades 

Una de las principales complicaciones que tuvimos al comenzar este proyecto fue habituarnos a la sintaxis de Scala, que en nuestra experiencia no se parece en nada a ningún lenguaje que hayamos visto anteriormente en la carrera.

Por otro lado fue bastante problemático acostumbrarse a trabajar con ``objetos`` desde una perspectiva funcional y numerosas veces nos veíamos tentados en utilizar la parte imperativa de Scala para por ejemplo recorrer un mapa o una lista, pero mientras más pasaba el tiempo era cada vez más fácil encarar las problemáticas que teníamos.

Dejando de lado las dificultades del Lenguaje lo más complicado por diferencia fue entender como funcionaba **Model** , porque nunca instanciabamos dicha clase y muchas cosas más,esa y mil preguntas más tuvimos. Cada una se fue resolviendo de a poco mientras investigabamos los conceptos que necesitabamos saber.

Por último, los objetos en REST siempre se manipulan a partir de la [URI](https://stackoverflow.com/questions/176264/what-is-the-difference-between-a-uri-a-url-and-a-urn). Es la URI y ningún otro elemento el identificador único de cada recurso de ese sistema REST. La URI nos facilita acceder a la información para su modificación o borrado, o, por ejemplo, para compartir su ubicación exacta con terceros.  


## Bibliografía 

1. [RESTful API](https://searchmicroservices.techtarget.com/definition/RESTful-API)  y  [Características REST](https://bbvaopen4u.com/es/actualidad/api-rest-que-es-y-cuales-son-sus-ventajas-en-el-desarrollo-de-proyectos)
2. 
## Integrantes 
* Gonzalo Gigena 
* Leandro Acosta 
* Christian Moreno
