# Practica 1. Preprocesado de documentos.

## Cómo lo hemos hechos

### Recorrer directorio
Cogemos el segundo parámetro de la terminal (El nombre del archivo), y creamos un File con ese nombre, con <listFiles()> obtenemos los archivos que cuelgan del directorio (ignorando subdirectorios), de esta forma tenemos el listado de archivos que analizaremos.

### Flags de los ejercicios
El parámetro de terminal 0 será donde ponemos el flag (-d, -l, -t), se llama a la función determinada dependiendo del flag que se haya puesto.

Dentro de cada función, se recorrerá la lista de archivos que teníamos, y por cada archivo utilizaremos la clase auxiliar DocumentAnalyzer, el cual nos permitirá a los datos necesarios encapsulando el analisis del documento del main, finalmente usaremos la clase auxiliar OutputHelper para mostrar en pantalla o guardar en csv los datos que nos aporta DocumentAnalyzer.

### DocumentAnalyzer

## ¿Qué ha hecho cada miembro?

### Adrián
* Apartado (-d)
* Clase DocumentAnalyzer.java
* Método csvWriterMetadata de OutputHelper

### Andrés
* Clase DocumentAnalyser.java
* Métodos csvWriter y print de OutputHelper
* Apartado (-l)
* Apartado (-t)