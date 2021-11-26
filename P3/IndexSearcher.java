package P3;

 import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
 import org.apache.lucene.analysis.standard.StandardAnalyzer;
 import org.apache.lucene.document.*;

 import org.apache.lucene.queryparser.classic.QueryParser;
 import org.apache.lucene.search.IndexSearcher;
 import org.apache.lucene.search.Query;
 
 import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;


public class Busqueda{
    String indexPAth = "./index";

    public static void main(String[] args) {
        Analyzer analyzer = new StandardAnalyzer();

        Similarity similarity = new BM25Similarity();

        indexSearch(analyzer, similarity);
    }



    public void indexSearch(Analyzer analyzer, Similarity similarity){
        IndexReader  reader = null;
        try{
            reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));

        }catch(IOException.e){
            
        }
    }
}

