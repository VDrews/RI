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

    // 2. Comparacion de filtros a un texto pequeño
    System.out.println("Inicio de la aplicacion de filtros");

    File tiny_text = new File("P2/text.txt");
    ArrayList<String> filteredTexts = new ArrayList();
    DocumentAnalyzer doc = new DocumentAnalyzer(tiny_text);
    for (int i = 1; i<=9;++i){
      filteredTexts.addAll(doc.applyDifferentFilter(i));
    }
    OutputHelper.txtWriterFromStringList("./P2/results/differentFiltersResults/differentFiltersResults", filteredTexts);
    System.out.println("Fin de la aplicacion de filtros");

    // 3. Analizador personalizado
    System.out.println("Inicio de la aplicacion de un analizador customizado");

    File file = new File("P2/text.txt");

    DocumentAnalyzer text = new DocumentAnalyzer(file);

    OutputHelper.csvWriter(text.contador("customAnalyzer"), "P2/results/3-text/" + text.getNombre());
    System.out.println("Fin de la aplicacion de un analizador customizado");
  
  
    //4. Elimnar los 4 ultimos caracteres de los token de un TokenStream. Utilizamos mismo texto que para 3.
    System.out.println("Inicio de la aplicacion de un filtro customizado");
    OutputHelper.txtWriterFromStringList("./P2/results/4-text/4-text", doc.last4CaractersFilter());
    System.out.println("Fin de la aplicacion de un filtro customizado");


  }



}
