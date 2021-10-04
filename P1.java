import org.apache.tika.sax.TeeContentHandler;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
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
    // Te dejo esto para que puedas recorrer el directorio,
    // son las rutas, tienes que crear un file para cada uno
    String eol = System.getProperty("line.separator");
    try {
      Writer writer = new FileWriter("metadata_table.csv");
      writer.append("Name;Type;Encoding;Language").append(eol);

      for (File f : files) {
        InputStream is = new FileInputStream(f); // creamos el inputstream
        Metadata metadata = new Metadata();
        AutoDetectParser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler();
        ParseContext context = new ParseContext();
        parser.parse(is, handler, metadata, context);
        writer.append(metadata.get(Metadata.RESOURCE_NAME_KEY)).append(";").append(metadata.get(Metadata.CONTENT_TYPE))
            .append(";").append(metadata.get(Metadata.CONTENT_ENCODING)).append(";")
            .append(metadata.get(Metadata.CONTENT_LANGUAGE)).append(";").append(eol); // CAMBIAR;

      }
      writer.close();
    } catch (IOException ex) {
      ex.printStackTrace(System.err);
    }

    // Realizar de fotma automatica una tabla que contenga el nombre del fichero,
    // tipo, codificacion e idioma.
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
