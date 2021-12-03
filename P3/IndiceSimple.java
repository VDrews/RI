package P3;

import org.apache.lucene.analysis.core.*;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.mchange.net.SocketUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.document.Field;

public class IndiceSimple {

    String indexpath = "./index";
    String docPath = "./datasets";
    boolean create = true;
    private IndexWriter writer;

    public static void main(String[] args) throws IOException, CsvException {

        StandardAnalyzer analyzer = new StandardAnalyzer();
        Similarity similarity = new ClassicSimilarity();
        IndiceSimple baseline = new IndiceSimple();

        baseline.configurarIndice(analyzer, similarity);

        File[] files;
        File directory = new File(args[0]);
        files = directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isFile();
            }
        });

        for (File file : files) {
            baseline.indexarDocumentos(file);
        }

        baseline.close();
    }

    // Método para configurar el indice.
    public void configurarIndice(Analyzer analyzer, Similarity similarity) throws IOException {
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setSimilarity(similarity);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        Directory dir = FSDirectory.open(Paths.get("./P3/index"));

        writer = new IndexWriter(dir, iwc);
    }

    public static String leerDocumento(File f) {
        return "";
    }

    /*
     * public static List<String[]> leerCsv(Reader reader) throws IOException,
     * CsvException { CSVReader csvReader = new
     * CSVReaderBuilder(reader).withSkipLines(1).build(); List<String[]> list = new
     * ArrayList<>(); list = csvReader.readAll(); reader.close(); csvReader.close();
     * return list; }
     */

    // Método para recoger la informacion de indexacion de los documentos, y
    // añadirlos al indice.
    public void indexarDocumentos(File file) throws FileNotFoundException, IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(file.getAbsoluteFile()));
        String subdoc[];
        reader.readNext(); // leemos la linea de headers sin recogerla.
        while ((subdoc = reader.readNext()) != null) {
            // new FileReader(fichero)
            Document doc = new Document();

            System.out.println(subdoc[HEADERS.AuthorsID]);
            System.out.println(subdoc[HEADERS.Title]);
            System.out.println(subdoc[HEADERS.Year]);
            System.out.println(subdoc[HEADERS.Abstract]);
            System.out.println(subdoc[HEADERS.AuthorKeywords]);

            // Los autores deberian dividirse
            // doc.add(new StringField("Authors", subdoc[HEADERS.Author], Field.Store.YES));
            final String[] authors = subdoc[HEADERS.Author].split(", ");
            String[] authors_complete = new String[authors.length];
            System.arraycopy(authors, 0, authors_complete, 0, authors.length); // copiamos los nombres completos de los
                                                                               // autores. vana ser indexados a parte

            List<String> autores = Arrays.asList(authors);
            ArrayList<String> author_Ngram = new ArrayList();

            for (int i = 0; i < autores.size(); ++i) {
                DocumentAnalyzer analyzer = new DocumentAnalyzer(autores.get(i));
                List<String> text = analyzer.applyDifferentFilter(6); // aplicamos //EdgeNGramFilter
                for (String str : text) {
                    author_Ngram.add(str);
                }
                // author_Ngram.add(authors[i]); YA NO ES NECESARIO PQ LO HAGO EN OTRO CAMPO
                // DIFERENTE EN LA LINEA 131
            }

            for (String author : author_Ngram) {
                if (author.length() > 4)
                    doc.add(new StringField("Author_Ngram", author, Field.Store.YES));
            }

            for (String author : authors_complete) {
                doc.add(new StringField("Author", author, Field.Store.YES));
            }

            final String[] keywords = subdoc[HEADERS.AuthorKeywords].split("; ");
            for (String keyword : keywords) {
                doc.add(new TextField("Keyword", keyword, Field.Store.YES));
            }

            doc.add(new TextField("Title", subdoc[HEADERS.Title], Field.Store.YES));
            doc.add(new IntPoint("Year", Integer.parseInt(subdoc[HEADERS.Year])));
            doc.add(new StoredField("Year", Integer.parseInt(subdoc[HEADERS.Year])));
            doc.add(new TextField("Abstract", subdoc[HEADERS.Abstract], Field.Store.YES));
            doc.add(new TextField("Keywords", subdoc[HEADERS.AuthorKeywords], Field.Store.YES));

            writer.addDocument(doc);

        }
    }

    // Método que maneja el cierre del indice.
    public void close() {
        try {
            writer.commit();
            writer.close();
        } catch (IOException e) {
            System.out.println("¡Error cerrando el indice!");
        }

    }

}
