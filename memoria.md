# Practica 1. Preprocesado de documentos.

## Cómo lo hemos hecho

### Recorrer directorio
 Para recorrer el directorio recuperamos el segundo parámetro de la terminal (El nombre del archivo), y creamos un File con ese nombre, con <listFiles()> obtenemos los archivos que cuelgan del directorio (ignorando subdirectorios), de esta forma tenemos el listado de archivos que analizaremos.

### Flags de los ejercicios
El parámetro de terminal 0 será donde ponemos el flag (-d, -l, -t). Se llama a la función determinada dependiendo del flag que se haya puesto en la linea de comandos.

Dentro de cada función, se recorrerá la lista de archivos que teníamos, y por cada archivo utilizaremos la clase auxiliar DocumentAnalyzer, el cual nos permitirá a los datos necesarios encapsulando el analisis del documento del main, finalmente usaremos la clase auxiliar OutputHelper para mostrar en pantalla o guardar en csv los datos que nos aporta DocumentAnalyzer.

### DocumentAnalyzer


### OutputHeklper
### Método csvWriter
### Método csvWriterMetadata
Este método recibe como parametros una lista con los nombres de los archivos, una lista con los lenguajes de los diferentes archivos del directorio, una lista de objetos Metadata con los metadatos de los archivos y un string que define el nombre del archivo generado como salida.

La finalidad de este método es generar un archivo ".csv" con los metadatos de lso archivos que se especifican en el guión de la práctica.

Para conseguirlo se hace uso de la clase FileWriter y su metodo append para primero añadir los titulos de la tabla y dentro de un bucle for añadir los diferentes valores de los metadatos accediento a los diferentes parámetros del método.
### Método print

## Resultados 
Los resultados de la práctica se componen 
## ¿Qué ha hecho cada miembro?

### Adrián
* Apartado (-d)
* Clase DocumentAnalyzer.java
* Método csvWriterMetadata de OutputHelper
* Memoria

### Andrés
* Clase DocumentAnalyser.java
* Métodos csvWriter y print de OutputHelper
* Apartado (-l)
* Apartado (-t)
* Memoria