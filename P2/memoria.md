# Práctica 2 Análisis de Texto

Para realizar la práctica hemos partido de nuestra clase ´´´DocumentAnalyzer.java´´´ para incorporar los diferentes análisis que se nos pide, de tal forma que quede la práctica más limpias y cohesionada.

## Apartado 1
En DocumentAnalyzer, hemos sobrecargado el método contador que teniamos en la práctica anterior, la cual tokenizaba las palabras de nuestro texto y las contaba, ahora nuestro método sobrecargado recibe también el tipo de analizador que queremos utilizar.

En nuestro caso hemos dado como opción estos analizadores:
* whiteAnalyzer
* simpleAnalyzer
* stopAnalyzer
* spanishAnalyzer
* standardAnalyzer (por defecto)

Además, también va incluido el analizador que hemos utilizado en la práctica 3.

Una vez seleccionado el analizador, obtenemos el contenido del archivo usando lo que ya teniamos en la clase de la práctica 1 (usamos Tika para obtenerlo).

Abrimos el TokenStream y la recorremos obteniendo todos los CharTermAttribute, por cada uno realizamos lo mismo que con el otro contador, los vamos guardando en un hashmap, y si el elemento ya ha sido introducido, se incrementa el valor asociado.

Finalmente, lo convertimos a array y lo ordenamos de igual forma que hicimos en la práctica anterior, por ese motivo, creamos el método privado ```hashMapToSortedArray()```.

Finalmente en el main, nos recorremos todos los archivos del directorio test, y por cada uno lo procesamos con los diferentes analizadores, y los guardamos en carpetas separadas por el analizador utilizado.ss

### Análisis
Para realizar el análisis tomaremos los csv de ideal como referencia, para cada uno de los analizadores

P2/
├─ results/
│  ├─ defaultAnalyzer/
│  │  ├─ catalog.xml.csv
│  │  ├─ ideal.html.csv
│  │  ├─ ....csv
│  ├─ simpleAnalyzer/
│  ├─ .../

* ```WhitespaceAnalyzer```: Nos encontramos con una lista con todas las palabras que nos podemos encontrar en el documento, ya que se ha tokenizado usando el espacio como separador, sin usarse ningún filtro
* ```StopAnalyzer```: En este caso se filtran las palabras vacías que tenemos indicadas en el documento ```dictionaries/stopwords.txt```, por tanto desaparecen las preposiciones, artículos, y otros términos que se consideran que no aportan semántica al texto. Podemos ver como hay palabras que no es capaz de filtrar, terminos extraidos que no existen en el castellano y por tanto tampoco encontraremos en nuestra lista de stopwords
* ```StandardAnalyzer```: En este caso, vemos que siguen apareciendo las stopwords a pesar de que este analizador debería ser capaz de filtrarlos, esto ocurre porque no le estamos indicando que use nuestra lista de stopwords, por tanto está utilizando su lista por defecto, la cual es una lista de stopwords para la lengua inglesa.
* ```SimpleAnalyzer```: Funciona de forma similar a WhitespaceAnalyzer, pero podemos ver que SimpleAnalyzer obtiene un mayor número de tokens que WhitespaceAnalyzer, esto se debe a que SimpleAnalyzer también tokeniza aquellas palabras que van separadas por signos de puntuación y no solamente por espacios.
* ```SpanishAnalyzer```: En este caso, nos elimina las palabras vacías correctamente (al usar por defecto las de castellano), y además simplifica las palabras a su raíz, aunque en este caso vemos que ciertos términos sin sentido se mantienen de igual forma que ocurría con el StopAnalyzer.

## Apartado 3

Para realizar el analizador, damos la opción en el método contador de usar nuestro propio analizador, de tal forma que nos queda este fragmento de código:

```javascript
analyzer = new Analyzer() {
  @Override
  protected TokenStreamComponents createComponents(String fieldName) {
    try {

      // Importamos los diccionarios
      InputStream affixStream = new FileInputStream("P2/dictionaries/es.aff");
      InputStream dictStream = new FileInputStream("P2/dictionaries/es.dic");

      // Carpeta temporal para el diccionario
      Directory directorioTemp = FSDirectory.open(Paths.get("P2/temp"));
      Dictionary dic = new Dictionary(directorioTemp, "temporalFile", affixStream, dictStream);

      // Tokenización
      Tokenizer source = new UAX29URLEmailTokenizer();

      // Filtramos las palabras vacías
      File stopWordsFile = new File("P2/dictionaries/stopwords.txt");
      DocumentAnalyzer stopWordsDocumentAnalyzer = new DocumentAnalyzer(stopWordsFile);
      Collection<String> stopWordsCollection = Arrays
          .asList(stopWordsDocumentAnalyzer.getContenido().split("\\r?\\n"));
      CharArraySet stopWords = new CharArraySet(stopWordsCollection, false);

      TokenStream result = new StopFilter(source, stopWords);

      result = new HunspellStemFilter(result, dic, true, true);

      return new TokenStreamComponents(source, result);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
};
```

Para ello hemos utilizado:

### Tokenizer
- ```UAX29URLEmailTokenizer```: Tokenizará nuestro texto incluyendo emails y URLS, aunque estos últimos desaparecerán con los siguientes filtros.
### Filtros
- ```StopFilter```: Filtrará las palabras vacías que le sindiquemos.
  - Hemos usado el archivo stopwords.txt, donde indicamos que palabras vacías queremos que se filtren
- ```HunspellStemFilter```: A diferencia de SpanishAnalyzer, en vez de obtener la raíz. tranforma las derivaciones de los tokens en la palabra principal, por ejemplo en verbos nos lo transformará a infinitivo
  Hemos usado los siguientes archivos adicionales para realizarlo:
  - Affix dictionary (es.aff): Lista con los diferentes prefijos y sufijos
  - Diccionario castellano (es.dic): Lista de palabras en castellano



## ¿Qué ha hecho cada miembro?

### Adrián
### Andrés

* Apartado 1
* Apartado 3
