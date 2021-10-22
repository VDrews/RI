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

    // 1. Estudio estadístico
    for (File f : files) {

      DocumentAnalyzer doc = new DocumentAnalyzer(f);

      OutputHelper.csvWriter(doc.contador("whiteAnalyzer"), "P2/results/whiteAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("simpleAnalyzer"), "P2/results/simpleAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("stopAnalyzer"), "P2/results/stopAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("spanishAnalyzer"), "P2/results/spanishAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("defaultAnalyzer"), "P2/results/defaultAnalyzer/" + doc.getNombre());
    }

    // Llamar a un método de la clase DocumentAnalyzer que procese el texto y
    // aplique los diferentes filtros.
    // Recoger los resultados en un vector de files.
    // File tiny_text = new File("P2/text.txt");
    // DocumentAnalyzer doc = new DocumentAnalyzer(f);

    // doc.applyDifferentFilters(tiny_text);
    // DocumentAnalyzer.applyDifferentFilters(tiny_text);

    // // 3. Analizador personalizado
    File file = new File("P2/text.txt");

    DocumentAnalyzer text = new DocumentAnalyzer(file);

    OutputHelper.csvWriter(text.contador("customAnalyzer"), "P2/results/3-text/" + text.getNombre());

  }

}
