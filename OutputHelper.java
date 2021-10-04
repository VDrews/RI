import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Map;

public class OutputHelper {
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

  public static void print(List<String> list) {
    for (String s : list) {
      System.out.println(s);
    }
  }
}
