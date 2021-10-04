import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.ArrayList;
import org.apache.tika.sax.Link;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.tika.metadata.Metadata;


public class OutputHelper {
  // Exporta un HashMap a CSV
  public static void csvWriter(List<Entry<String, Integer>> entries, String pathname) throws IOException {
    String eol = System.getProperty("line.separator");

    try (Writer writer = new FileWriter(pathname + ".csv")) {
      writer.append("Text;Size").append(eol);
      for (Entry<String, Integer> entry : entries) {
        writer.append(entry.getKey()).append(',').append(Integer.toString(entry.getValue())).append(eol);
      }
    } catch (IOException ex) {
      ex.printStackTrace(System.err);
    }
  }

  //Exporta a tabla con titulo, tipo, codificacion y lenguaje.
  public static void csvWriterMetadata(ArrayList<Metadata> metadatos, String pathname) throws IOException {
    String eol = System.getProperty("line.separator");

    try (Writer writer = new FileWriter(pathname + ".csv")) {
      writer.append("Name;Type;Encoding;Language").append(eol);
      for (Metadata metadata_object : metadatos) {
        writer.append(metadata_object.get("resourceName")).append(";").append(metadata_object.get("Content-Type")).append(";").
        append(metadata_object.get("Content-Encoding")).append(metadata_object.get("Content-Encoding")).append(eol);//todo
      }
    } catch (IOException ex) {
      ex.printStackTrace(System.err);
    }
  }

  public static void print(List<Link> links, String pathname) {
    System.out.println(pathname.toUpperCase());
    for (Link link : links) {
      System.out.println(link.getUri());
    }
    System.out.println("\n\n");
  }
}
