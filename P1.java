import org.apache.tika.sax.TeeContentHandler;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class P1 {
  // Exporta un HashMap a CSV
  public static void csvWriter(Map<String, Integer> hashmap, String pathname) throws IOException {
    String eol = System.getProperty("line.separator");

    try (Writer writer = new FileWriter(pathname + ".csv")) {
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
      for (String pathname : pathnames) {
        System.out.println(pathname);
      }

      // Realizar de fotma automatica una tabla que contenga el nombre del fichero,
      // tipo, codificacion e idioma.
    } else if (args[0].equals("-l")) {
      for (String pathname : pathnames) {
        System.out.println(pathname);
      }
      File f = new File(args[1]);

      InputStream is = new FileInputStream(f);

      // Recuperar todos los enlaces
    } else if (args[0].equals("-t")) {
      for (String pathname : pathnames) {
        File f = new File(pathname);

        String text = tika.parseToString(f);
        String[] parts = text.split(" ");
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (String w : parts) {
          Integer n = map.get(w);
          n = (n == null) ? 1 : ++n;
          map.put(w, n);
        }
        csvWriter(map, pathname);
      }

    } else {
      System.out.println("Número de argumentos incorrectos. Argumentos: -d ; -l ; -t");
    }
  }
}
