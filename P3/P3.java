package P3;
import org.apache.lucene.analysis.core.*;

import java.io.IOException;

import com.mchange.net.SocketUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.index.IndexWriter;


public class P3 {

String indexpath = "./index";
String docPath = "./datasets";
boolean  create = true;
private IndexWriter writer;


    public static void main(String[] args) {
        
    }
    
    //Método para configurar el indice.
    public void configurarIndice(Analyzer analyzer,Similarity Similarity){
    
    }
    //Método para recoger la informacion de indexacion de los documentos, y añadirlos al indice.
    public void indexarDocumentos(){

    }

    //Método que maneja el cierre del indice.
    public void close(){
        try{
            writer.commit();
            writer.close();
        }catch(IOException e){
        System.out.println("¡Error cerrando el indice!")
        }

    }


    
}
