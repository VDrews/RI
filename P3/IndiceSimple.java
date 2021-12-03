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

import org.apache.lucene.facet.*;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
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
    private DirectoryTaxonomyWriter facet_writer;

    public static void main(String[] args) throws IOException, CsvException {

        Analyzer analyzer = new StandardAnalyzer();
        Similarity similarity = new ClassicSimilarity();
        IndiceSimple baseline = new IndiceSimple();
        IndiceSimple facet_index = new IndiceSimple();
        FacetsConfig fconfig = facet_index.configurarIndice(analyzer);
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
            baseline.indexarDocumentos(file,fconfig);
        }
        

        baseline.close();
    }

    // Método para configurar el indice principal
    public void configurarIndice(Analyzer analyzer, Similarity similarity) throws IOException {
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setSimilarity(similarity);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);

        Directory dir = FSDirectory.open(Paths.get("./P3/index"));

        writer = new IndexWriter(dir, iwc);
    }
        // Método para configurar el indice de las facetas
    public FacetsConfig configurarIndice(Analyzer analyzer) throws IOException {
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        FacetsConfig fconfig = new FacetsConfig();
        fconfig.setMultiValued("Author", true);
        fconfig.setMultiValued("Year", true);

        Directory dir = FSDirectory.open(Paths.get("./P3/facets"));

         facet_writer= new DirectoryTaxonomyWriter(dir);
         return fconfig;
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
    public void indexarDocumentos(File file, FacetsConfig fconfig) throws FileNotFoundException, IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(file.getAbsoluteFile()));
        String subdoc[];
        reader.readNext(); // leemos la linea de headers sin recogerla.
        while ((subdoc = reader.readNext()) != null) {
            // new FileReader(fichero)
            Document doc = new Document();

            final String[] authors = subdoc[HEADERS.Author].split(", ");
            String[] authors_complete = new String [authors.length]; 
            System.arraycopy(authors, 0, authors_complete, 0, authors.length); // copiamos los nombres completos de los autores. vana  ser indexados a parte

            List<String> autores = Arrays.asList(authors);
            ArrayList<String> author_Ngram =  new ArrayList();

            // INCLUIMOS LOS CAMPOS DE INDEXACION
            for(int i = 0; i< autores.size(); ++i ){
                DocumentAnalyzer analyzer = new DocumentAnalyzer(autores.get(i));
                List<String> text = analyzer.applyDifferentFilter(6); // aplicamos //EdgeNGramFilter
                for(String str :text){
                    author_Ngram.add(str);
                }
                //author_Ngram.add(authors[i]); YA NO ES NECESARIO PQ LO HAGO EN OTRO CAMPO DIFERENTE EN LA LINEA 131
            }
            
            for (String author : author_Ngram) {
                if(author.length()>4)
                    doc.add(new StringField("Author_Ngrams", author, Field.Store.YES));
            }

            for (String author : authors_complete) {
                doc.add(new StringField("Author", author, Field.Store.YES));
            }
            
            

            doc.add(new TextField("Title", subdoc[HEADERS.Title], Field.Store.YES));
            //doc.add(new IntPoint("Year", Integer.parseInt(subdoc[HEADERS.Year])));
            doc.add(new TextField("Abstract", subdoc[HEADERS.Abstract], Field.Store.YES));


            // INCLUIMOS LAS FACETAS

            final String[] keywords = subdoc[HEADERS.AuthorKeywords].split("; ");
            for (String keyword : keywords) {
                doc.add(new FacetField("Keyword", keyword));
            }

            doc.add(new FacetField("Year",subdoc[HEADERS.Year]));
            

            //ESCRIBIMOS EL INDICE
            writer.addDocument(fconfig.build(facet_writer,doc));

        }
    }

    // Método que maneja el cierre los índices.
    public void close() {
        try {
            writer.commit();
            writer.close();
            facet_writer.commit();
            facet_writer.close();
        } catch (IOException e) {
            System.out.println("¡Error cerrando el indice principal o el indice de las facetas!");
        }

    }

}
