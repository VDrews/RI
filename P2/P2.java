package P2;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

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
    System.out.println("Inicio del estudio estadistico");

    for (File f : files) {

      DocumentAnalyzer doc = new DocumentAnalyzer(f);

      OutputHelper.csvWriter(doc.contador("whiteAnalyzer"), "P2/results/whiteAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("simpleAnalyzer"), "P2/results/simpleAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("stopAnalyzer"), "P2/results/stopAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("spanishAnalyzer"), "P2/results/spanishAnalyzer/" + doc.getNombre());
      OutputHelper.csvWriter(doc.contador("defaultAnalyzer"), "P2/results/defaultAnalyzer/" + doc.getNombre());
    }
    System.out.println("Fin del estudio estadistico");

<<<<<<< Updated upstream
    // Llamar a un método de la clase DocumentAnalyzer que procese el texto y
    // aplique los diferentes filtros.
    // Recoger los resultados en un vector de files.
    // File tiny_text = new File("P2/text.txt");
    // DocumentAnalyzer doc = new DocumentAnalyzer(f);

    // doc.applyDifferentFilters(tiny_text);
    // DocumentAnalyzer.applyDifferentFilters(tiny_text);

    // // 3. Analizador personalizado
=======
    // 2. Comparacion de filtros a un texto pequeño
    System.out.println("Inicio de la aplicacion de filtros");

    File tiny_text = new File("P2/text.txt");
    ArrayList<String> filteredTexts = new ArrayList();
    DocumentAnalyzer doc = new DocumentAnalyzer(tiny_text);
    for (int i = 1; i<=8;++i){
      filteredTexts.addAll(doc.applyDifferentFilter(i));
    }
    OutputHelper.txtWriterFromStringList("./P2/results/differentFiltersResults/differentFiltersResults", filteredTexts);
    System.out.println("Fin de la aplicacion de filtros");

    // 3. Analizador personalizado
    System.out.println("Inicio de la aplicacion de un analizador customizado");

>>>>>>> Stashed changes
    File file = new File("P2/text.txt");

    DocumentAnalyzer text = new DocumentAnalyzer(file);

<<<<<<< Updated upstream
    OutputHelper.csvWriter(text.contador("customAnalyzer"), "P2/results/3-text/" + text.getNombre());
=======
    OutputHelper.csvWriter(text.contador("customAnalyzer"), "P2/results/2-text/" + text.getNombre());
    System.out.println("Fin de la aplicacion de un analizador customizado");
>>>>>>> Stashed changes

  }

}
