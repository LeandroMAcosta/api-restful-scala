# API REST

## Indice 

1. Introducción
2. ¿Qué es una API RESTful? 
3. Nuestro Modelo 

    3.1 Herencia
    
    3.2 Base de Datos

4. Dificultades
5. Bibliografía 
6. Integrantes 

## Introducción 

En este laboratorio vamos a implementar una API RESTful para un servicio de delivery, al estilo Rappi, PedidosYA,ifood o Deliveroo. En esta API, los proveedores suben sus productos, que los consumidores pueden ver y seleccionar para incluir en un pedido. Una vez que el pedido está finalizado, la aplicación se encarga de cobrarlo al consumidor, entregarlo y pagarle al proveedor el dinero correspondiente.
º

## ¿Qué es una API REST?
Una API RESTful, también conocida como servicio web RESTful,se basa en la tecnología de transferencia de estado representacional (**REST**). 

REST es cualquier interfaz entre sistemas que use HTTP para obtener datos o generar operaciones sobre esos datos en todos los formatos posibles como por ejemplo XML (en nuestro caso vamos a trabajar con JSON).

Posee muchas ventajas, entre las que se destacan más es que es un protocolo cliente/servidor ``sin estados``, cada petición HTTP contiene toda la información necesaria para ejecutarla, por lo tanto ni el cliente ni el servidor necesitan recordar ningún estado para satisfacer dicha petición.

>Las operaciones más importantes relacionadas con los datos en cualquier sistema REST son:POST (crear), GET (leer y consultar), PUT (editar) y DELETE (eliminar). En este proyecto sólo utilizamos POST y GET.

Por último, los objetos en REST siempre se manipulan a partir de la [URI](https://stackoverflow.com/questions/176264/what-is-the-difference-between-a-uri-a-url-and-a-urn). Es la URI y ningún otro elemento el identificador único de cada recurso de ese sistema REST. La URI nos facilita acceder a la información para su modificación o borrado, o, por ejemplo, para compartir su ubicación exacta con terceros.  

## Nuestro Modelo

### Herencia 

Al principio nosotros optamos porque todos los modelos sean "hijos" de Models incluyendo a ``User``(Consumer y Provider eran hijos de User) para simplificarnos la vida, sin embargo no tardamos mucho en darnos cuenta que esa no era la forma correcta, ya que, de dicha forma Consumer y Provider compartían la misma base de datos, por lo tanto cuando por ejemplo queríamos buscar un el nombre de un consumidor y por alguna casualidad había un proveedor con el mismo nombre nos iba a devolver el proveedor. Para solucionarlo optamos por el siguiente esquema:

![database](diagrams/herencia.png)

También cabe destacar que ni Models ni User son una clase, ni siquiera una clase Abstracta, si no un *trait*. Los  trait's tienen muchas propiedade interesantes, pero la que se destaca por sobre las demas es la herencia múltiple, sin ella por ejemplo no podríamos hacer el proyecto de la forma que lo hicimos, puesto que, si vemos la imagen anterior Consumer y Provider ambos heredan de User y de Models.

En la programación orientada a objetos, la **Herencia** permite a nuevos objetos obtener las propiedades de objetos ya existentes. Una clase que es usada como la base para herencia se llama "*superclass*" o clase base y una clase que hereda de una clase base se denomina "*subclass*". Si nosotros no pudiesemos hacer esto para poder obtener todas las propiedades de la clase padre tendríamos que copiar todos los métodos y atributos que querramos en nuestra clase hijo, lo que haría que las clases tenga un tamaño grande, ya que, generalmente se le agregan nuevos métodos o atributos.

### Base de Datos

La siguiente imagen muestra como se relacionan las bases de datos de los distintos Modelos.

![database](diagrams/database.png)
 
Cuando trabajamos con la base de datos descubrimos un problema, al ser todos los parametros de nuestros modelos de tipo **val** cuando queríamos modificar por ejemplo el estado de nuestra orden teníamos que crear una nueva instancia de tipo order y copiar los datos de la orden anterior pero con el estado nuevo, pero ¿Cuál es el problema? , cuando creabamos la nueva instancia, dicha instancia iba a tener un nuevo ``ID`` , entonces si queríamos hacer un un GET del ID viejo no ibamos a encontrar la Orden. También esto sucede si queremos cambiar algún dato del Consumer o el Provider como por ejemplo el balance. Para poder solucionarlo tuvimos que declar los parametros que queríamos modificar como **var** para que dejen de ser inmutables. 


## Dificultades 

Una de las principales complicaciones que tuvimos al comenzar este proyecto fue habituarnos a la sintaxis de Scala, que en nuestra experiencia no se parece en nada a ningún lenguaje que hayamos visto anteriormente en la carrera.

Por otro lado fue bastante problemático acostumbrarse a trabajar con ``objetos`` desde una perspectiva funcional y numerosas veces nos veíamos tentados en utilizar la parte imperativa de Scala para por ejemplo recorrer un mapa o una lista, pero mientras más pasaba el tiempo era cada vez más fácil encarar las problemáticas que teníamos.

Además no fue fácil acostumbrarse a la forma en la que están encapsulados los objetos en Scala, debido a que, no podíamos acceder a los parámetros de forma directa y teníamos que crear nuevos métodos específicamente para obtener ciertos atributos. Sin embargo con el tiempo comprendimos que esa era la mejor forma de hacerlo y que si Scala nos permitiera hacerlo perderíamos la abstracción que nos provee la programación orientada a objetos.

Dejando de lado las dificultades del Lenguaje lo más complicado por diferencia fue entender como funcionaba **Model** , porque nunca instanciabamos dicha clase y muchas cosas más,esa y mil preguntas más tuvimos. Cada una se fue resolviendo de a poco mientras investigabamos los conceptos que necesitabamos saber. Después de esto tuvimos un error de diseño (explicado más en profuncidad en "Herencia") donde decidimos que User herede de Models, para solucionarlo no tuvimos que cambiar muchas cosas, pero si lidiamos con muchos errores al compilar.

Finalmente tuvimos una complicación cunado queríamos implementar el cambio del balance en el Consumidor y el Proveedor cuando se realiza una orden. El problema que teníamos venía de que User era una ``case class`` y a pesar que uno de sus atributos era de tipo var cuando lo modificamos en alguna subclase nos decía que era un valor inmutable.Todo esto se solucionó cambiadolo por un trait.

>Las clases de tipo **case** son como las clases normales, nada más que tienen pequeñas diferencias. Primero que todo este tipo de clases son buenas para modelar datos inmutables, también tienen un método por defecto (apply()) que se encarga de la construcción del objeto.

## Bibliografía 

1. [RESTful API](https://searchmicroservices.techtarget.com/definition/RESTful-API)  y  [Características REST](https://bbvaopen4u.com/es/actualidad/api-rest-que-es-y-cuales-son-sus-ventajas-en-el-desarrollo-de-proyectos)
2. [Inheritance](https://www.adobe.com/devnet/actionscript/learning/oop-concepts/inheritance.html)
3. [Abstract Classes and Traits](https://www.geeksforgeeks.org/difference-between-traits-and-abstract-classes-in-scala/)
4. [Case Classes](https://docs.scala-lang.org/tour/case-classes.html)

## Integrantes 
* Gonzalo Gigena 
* Leandro Acosta 
* Christian Moreno
