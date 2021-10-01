import org.apache.tika.sax.TeeContentHandler;
import java.io.File;
import java.io.FileInputStream;

import org.apache.tika.Tika;
import org.apache.tika.metadata.Metadata;
import java.io.InputStream;

public class P1 {
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

    } else {
      System.out.println("Número de argumentos incorrectos. Argumentos: -d ; -l ; -t");
    }
  }

}
