import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class DocumentAnalyzer {
  private static Tika tika = new Tika();
  private File file;

  DocumentAnalyzer(File file) {
    this.file = file;
  }

  public List<String> listaEnlaces() throws Exception {
    List<String> urls = new ArrayList<String>();
    String text = tika.parseToString(this.file);
    Matcher m = Pattern.compile(
        "(https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|www\\.[a-zA-Z0-9][a-zA-Z0-9-]+[a-zA-Z0-9]\\.[^\\s]{2,}|https?:\\/\\/(?:www\\.|(?!www))[a-zA-Z0-9]+\\.[^\\s]{2,}|www\\.[a-zA-Z0-9]+\\.[^\\s]{2,})")
        .matcher(text);
    while (m.find()) {
      urls.add(m.group());
    }
    return urls;
  }

  public Map<String, Integer> contador() throws IOException, TikaException {
    String text = tika.parseToString(this.file);
    String[] parts = text.split(" ");
    Map<String, Integer> map = new HashMap<String, Integer>();
    for (String w : parts) {
      final String word = w.toLowerCase();
      Integer n = map.get(word);
      n = (n == null) ? 1 : ++n;
      if (Pattern.matches("\\w{3,}", word))
        map.put(word, n);
    }

    return map;
  }
}