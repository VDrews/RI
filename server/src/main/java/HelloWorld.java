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
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 *
 * @author andresgarciamacias
 */
import io.javalin.Javalin;

public class HelloWorld {
    private static Gson gson = new Gson();
    private static Directory dir;
    private static IndexReader reader;
    private static IndexSearcher searcher;

    private static String DocsToJSON(ScoreDoc[] hits) throws IOException {
        ArrayList<Doc> docs = new ArrayList<Doc>();
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            docs.add(new Doc(doc.get("Title"), doc.get("Content"), doc.get("Year")));
        }
        return gson.toJson(docs, new TypeToken<ArrayList<Doc>>() {
        }.getType());

    }

    public static void main(String[] args) throws IOException, ParseException {
        Javalin app = Javalin.create().start(7030);

        dir = FSDirectory.open(Paths.get("./server/index"));
        reader = DirectoryReader.open(dir);
        searcher = new IndexSearcher(reader);

        QueryParser parser = new QueryParser("Content", new StandardAnalyzer());

        app.get("/{text}", ctx -> {
            Query q1;
            q1 = parser.parse("Content:" + ctx.pathParam("text"));
            TopDocs topdocs = searcher.search(q1, 20);
            ScoreDoc[] hits = topdocs.scoreDocs;

            ctx.result(DocsToJSON(hits));
        });
    }
}
