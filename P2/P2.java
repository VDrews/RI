package P2;

import java.io.File;
import java.io.FileFilter;

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
    System.out.println(files.length);

    // 1. Estudio estadístico
    for (File f : files) {

      DocumentAnalyzer doc = new DocumentAnalyzer(f);

      OutputHelper.csvWriter(doc.contador("whiteAnalyzer"), "P2/results/whiteAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("simpleAnalyzer"), "P2/results/simpleAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("stopAnalyzer"), "P2/results/stopAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("spanishAnalyzer"), "P2/results/spanishAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("defaultAnalyzer"), "P2/results/defaultAnalyzer/" + doc.getNombre());
    }

    // 3. Analizador personalizado
    File file = new File("P2/text.txt");

    DocumentAnalyzer text = new DocumentAnalyzer(file);

    OutputHelper.csvWriter(text.contador("customAnalyzer"), "P2/results/2-text/" + text.getNombre());

  }

}
