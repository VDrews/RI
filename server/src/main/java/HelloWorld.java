/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.Paths;

/**
 *
 * @author andresgarciamacias
 */
import io.javalin.Javalin;

public class HelloWorld {

    public static void main(String[] args) throws IOException, ParseException {
        Javalin app = Javalin.create().start(7030);
        Directory dir;
        IndexReader reader;
        dir = FSDirectory.open(Paths.get("./server/index"));
        reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser("Content", new StandardAnalyzer());

        app.get("/{text}", ctx -> {
            Query q1;
            System.out.println(ctx.pathParam("text"));
            q1 = parser.parse("Content:" + ctx.pathParam("text"));
            TopDocs docs = searcher.search(q1, 20);
            System.out.println("Docs encontrados: " + docs.totalHits);

            ctx.result("Docs encontrados: " + docs.totalHits);
        });
    }
}
