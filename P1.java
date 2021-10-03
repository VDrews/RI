import org.apache.tika.sax.TeeContentHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.apache.tika.parser.Parser;
import org.apache.tika.metadata.Metadata;

public class P1 {
  // Exporta un HashMap a CSV
  public static void csvWriter(Map<String, Integer> hashmap, String pathname) throws IOException {
    String eol = System.getProperty("line.separator");

    try (Writer writer = new FileWriter(pathname + ".csv")) {
      writer.append("Text;Size").append(eol);
      for (Map.Entry<String, Integer> entry : hashmap.entrySet()) {
        writer.append(entry.getKey()).append(',').append(Integer.toString(entry.getValue())).append(eol);
      }
    } catch (IOException ex) {
      ex.printStackTrace(System.err);
    }
  }

  public static void main(String[] args) throws Exception {
    Tika tika = new Tika();
    String[] pathnames;
    File f_pathnames = new File(args[1]);
    pathnames = f_pathnames.list();
    System.out.println(args[0]);

    if (args[0].equals("-d")) {
      // Te dejo esto para que puedas recorrer el directorio,
      // son las rutas, tienes que crear un file para cada uno
      String eol = System.getProperty("line.separator");
      try{
        Writer writer = new FileWriter("metadata_table.csv");
        writer.append("Name;Type;Encoding;Language").append(eol);

        for (String pathname : pathnames) {
          System.out.println("Procesando: "+pathname);
          File f = new File(pathname); //abrimos archivo
          InputStream is = new FileInputStream(f); //creamos el inputstream
          Metadata metadata = new Metadata(); 
          AutoDetectParser parser = new AutoDetectParser();
          BodyContentHandler handler = new BodyContentHandler( );
          ParseContext context = new ParseContext();
          parser.parse(is, handler, metadata, context);        
          writer.append(metadata.get(Metadata.RESOURCE_NAME_KEY)).append(";").append(metadata.get(Metadata.CONTENT_TYPE)).
          append(";").append(metadata.get(Metadata.CONTENT_ENCODING)).append(";").append(metadata.get(Metadata.CONTENT_LANGUAGE)).append(";").append(eol); //CAMBIAR;
          
        }
    }catch(IOException ex){
      ex.printStackTrace(System.err);
    }

      // Realizar de fotma automatica una tabla que contenga el nombre del fichero,
      // tipo, codificacion e idioma.
      
    } else if (args[0].equals("-l")) {
      for (String pathname : pathnames) {
        File f = new File(pathname);

        List<String> urls = new ArrayList<String>();
        String text = tika.parseToString(f);
        Matcher m = Pattern.compile(
            "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})")
            .matcher(text);
        while (m.find()) {
          urls.add(m.group());
        }
        for (String url : urls) {
          System.out.println(url);
        }
      }

      // Recuperar todos los enlaces
    } else if (args[0].equals("-t")) {
      for (String pathname : pathnames) {
        File f = new File(pathname);

        String text = tika.parseToString(f);
        String[] parts = text.split(" ");
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String w : parts) {
          final String word = w.toLowerCase();
          Integer n = map.get(word);
          n = (n == null) ? 1 : ++n;
          if (Pattern.matches("\\w{3,}", word))
            map.put(word, n);
        }
        csvWriter(map, pathname);
      }

    } else {
      System.out.println("Número de argumentos incorrectos. Argumentos: -d ; -l ; -t");
    }
  }
}
