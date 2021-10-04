import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.Link;
import org.apache.tika.sax.LinkContentHandler;
import org.apache.tika.sax.TeeContentHandler;

public class DocumentAnalyzer {
  private static Tika tika = new Tika();
  private File file;
  private String nombre;
  private String contenido;
  private String[] metadata;
  private List<Link> enlaces;

  String getNombre() {
    return nombre;
  }

  List<Link> getEnlaces() {
    return enlaces;
  }

  DocumentAnalyzer(File file) throws Exception {
    this.file = file;
    this.nombre = file.getName();
    FileInputStream inputStream = new FileInputStream(file); // creamos el inputstream
    BodyContentHandler contentHandler = new BodyContentHandler(-1);
    Metadata metadata = new Metadata();
    ParseContext parser = new ParseContext();
    LinkContentHandler linkContentHandler = new LinkContentHandler();
    TeeContentHandler teeContentHandler = new TeeContentHandler(linkContentHandler, contentHandler);
    AutoDetectParser autodetectParser = new AutoDetectParser();

    autodetectParser.parse(inputStream, teeContentHandler, metadata, parser);

    this.contenido = contentHandler.toString();
    this.metadata = metadata.names();

    this.enlaces = linkContentHandler.getLinks();
  }

  // public List<String> listaEnlaces() throws Exception {
  // List<String> urls = new ArrayList<String>();
  // String text = tika.parseToString(this.file);
  // Matcher m = Pattern.compile(
  // "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})")
  // .matcher(text);
  // while (m.find()) {
  // urls.add(m.group());
  // }
  // return urls;
  // }

  public List<Entry<String, Integer>> contador() throws IOException, TikaException {
    String[] parts = this.contenido.split(" ");
    Map<String, Integer> map = new HashMap<String, Integer>();
    for (String w : parts) {
      final String word = w.toLowerCase();
      Integer n = map.get(word);
      n = (n == null) ? 1 : ++n;
      if (Pattern.matches("\\w{3,}", word))
        map.put(word, n);
    }

    Set<Entry<String, Integer>> entries = map.entrySet();
    Comparator<Entry<String, Integer>> valueComparator = new Comparator<Entry<String, Integer>>() {
      @Override
      public int compare(Entry<String, Integer> e1, Entry<String, Integer> e2) {
        Integer v1 = e1.getValue();
        Integer v2 = e2.getValue();
        return v2.compareTo(v1);
      }
    };

    List<Entry<String, Integer>> orderedList = new ArrayList<Entry<String, Integer>>(entries);

    Collections.sort(orderedList, valueComparator);
    return orderedList;
  }
}