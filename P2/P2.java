/*package P2;

import java.io.File;
import java.io.FileFilter;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class P2 {
  public static void main(String[] args) throws Exception {

    File[] files;
    File directory = new File(args[0]);
    files = directory.listFiles(new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.isFile();
      }
    });

    // 1. Comparar Analizadores
    for (File f : files) {
      WhitespaceAnalyzer an = new WhitespaceAnalyzer();
      DocumentAnalyzer document = new DocumentAnalyzer(f);
      TokenStream stream = an.tokenStream(null, document.getContenido());

      stream.reset();
      while (stream.incrementToken()) {
        System.out.println(stream.getAttribute(CharTermAttribute.class));
      }
      stream.end();
      stream.close();
    }
  }
}
*/