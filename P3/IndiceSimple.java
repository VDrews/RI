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
import java.util.List;

import com.mchange.net.SocketUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
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

        Analyzer analyzer = new StandardAnalyzer();
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

    public static List<String[]> leerCsv(Reader reader) throws IOException, CsvException {
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        List<String[]> list = new ArrayList<>();
        list = csvReader.readAll();
        reader.close();
        csvReader.close();
        return list;
    }

    // Método para recoger la informacion de indexacion de los documentos, y
    // añadirlos al indice.
    public void indexarDocumentos(File file) throws FileNotFoundException, IOException, CsvException {

        // String cadena = leerDocumento(d);
        List<String[]> documentos = leerCsv(new FileReader(file.getAbsoluteFile()));
        for (String[] subdoc : documentos) {

            // new FileReader(fichero)
            Document doc = new Document();
            System.out.println(subdoc[0]);
            System.out.println(subdoc[2]);
            System.out.println(subdoc[3]);
            System.out.println(subdoc[16]);
            System.out.println(subdoc[17]);
            // Los autores deberian dividirse
            doc.add(new StringField("Authors", subdoc[0], Field.Store.YES));
            doc.add(new TextField("Title", subdoc[2], Field.Store.YES));
            doc.add(new IntPoint("Year", Integer.parseInt(subdoc[3])));
            doc.add(new TextField("Content", subdoc[16], Field.Store.YES));
            doc.add(new TextField("Keywords", subdoc[17], Field.Store.YES));
            // Integer start = ?;
            // Integer end = ?;

            // String aux = cadena.substring(start, end);

            // doc.add(new IntPoint("ID", valor));
            // doc.add(new StoredField("ID", valor));

            // start = ...;
            // end = ...;

            // String cuerpo = cadena.substring(start, end);

            // doc.add(new TextField("Body", cuerpo, Field.Store.YES));

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
