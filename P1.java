import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import java.io.InputStream;
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;


public class P1 {
  public static void main(String[] args) throws Exception {

    if (args.length != 2) {
      throw new IllegalArgumentException("Número de argumentos incorrectos. Argumentos: -d ; -l ; -t");
    }

    File[] files;
    File directory = new File(args[1]);
    files = directory.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.isFile();
      }
    });

    if (args[0].equals("-d")) {
      generarTabla(files);

    } else if (args[0].equals("-l")) {
      generarEnlaces(files);
      // Recuperar todos los enlaces
    } else if (args[0].equals("-t")) {
      generarContador(files);

    } else {
      throw new IllegalArgumentException("Número de argumentos incorrectos. Argumentos: -d ; -l ; -t");
    }
  }

  public static void generarTabla(File[] files) throws Exception {
    // Realizar de fotma automatica una tabla que contenga el nombre del fichero,
    // tipo, codificacion e idioma.
    // Te dejo esto para que puedas recorrer el directorio,
    // son las rutas, tienes que crear un file para cada uno
    String eol = System.getProperty("line.separator");
    ArrayList<Metadata> metadata_list = new ArrayList<Metadata>();

      for (File f : files) {
        System.out.println("Procesando: " + f.getName());
        DocumentAnalyzer document = new DocumentAnalyzer(f);
        metadata_list.add(document.getMetadata());
      }
      System.out.println("Generando tabla de metadatos...");
      OutputHelper.csvWriterMetadata(metadata_list,"metadataTable");
  }

  public static void generarEnlaces(File[] files) throws Exception {
    for (File file : files) {
      DocumentAnalyzer document = new DocumentAnalyzer(file);
      OutputHelper.print(document.getEnlaces(), document.getNombre());
    }
  }

  public static void generarContador(File[] files) throws Exception {
    for (File file : files) {
      DocumentAnalyzer document = new DocumentAnalyzer(file);
      OutputHelper.csvWriter(document.contador(), document.getNombre());

    }
  }
}
