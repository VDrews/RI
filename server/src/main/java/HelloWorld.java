
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import org.apache.lucene.facet.DrillDownQuery;
import org.apache.lucene.facet.DrillSideways;
import org.apache.lucene.facet.FacetResult;
import org.apache.lucene.facet.Facets;
import org.apache.lucene.facet.FacetsCollector;
import org.apache.lucene.facet.FacetsConfig;
import org.apache.lucene.facet.LabelAndValue;
import org.apache.lucene.facet.DrillSideways.DrillSidewaysResult;
import org.apache.lucene.facet.taxonomy.*;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyReader;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
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
import java.util.Arrays;
import java.util.List;

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
    private static Directory taxoDir;
    private static IndexReader reader;
    private static IndexSearcher searcher;
    private static TaxonomyReader taxoReader;
    private static FacetsCollector fc;
    private static FacetsConfig fconfig;

    private static String DocsToJSON(ScoreDoc[] hits, List<FacetResult> TodasDims) throws IOException {

        List<LabelAndValue> keyword = Arrays.asList(TodasDims.get(0).labelValues);
        List<LabelAndValue> year = Arrays.asList(TodasDims.get(1).labelValues);

        // for (FacetResult fr : TodasDims) {
        // System.out.println("Categoria " + fr.dim);
        // for (LabelAndValue lv : fr.labelValues) {
        // System.out.println(" Etiq: " + lv.label + ",valor (#n)−>" + lv.value);
        // }
        // }

        ArrayList<Doc> docs = new ArrayList<Doc>();
        for (ScoreDoc hit : hits) {
            Document doc = searcher.doc(hit.doc);
            System.out.println(doc.get("Author"));
            docs.add(new Doc(doc.get("Title"), doc.get("Content"), doc.get("Year"), doc.getValues("Author"),
                    doc.getValues("Keyword"), doc.get("EID")));
        }

        String docsJSON = gson.toJson(docs, new TypeToken<ArrayList<Doc>>() {
        }.getType());
        String yearJSON = gson.toJson(year, new TypeToken<List<LabelAndValue>>() {
        }.getType());
        String keywordsJSON = gson.toJson(keyword, new TypeToken<List<LabelAndValue>>() {
        }.getType());

        return "{ \"docs\":" + docsJSON +
                ",\"year\":" + yearJSON +
                ",\"keyword\":" + keywordsJSON +
                "}";

    }

    public static void main(String[] args) throws IOException, ParseException {
        Javalin app = Javalin.create().start(7030);

        dir = FSDirectory.open(Paths.get("./server/index"));
        taxoDir = FSDirectory.open(Paths.get("./server/facets"));
        reader = DirectoryReader.open(dir);
        searcher = new IndexSearcher(reader);

        fc = new FacetsCollector();

        taxoReader = new DirectoryTaxonomyReader(taxoDir);

        QueryParser parser = new QueryParser("Content", new StandardAnalyzer());
        FacetsConfig fconfig = new FacetsConfig();

        // fconfig.setMultiValued("Author", true);
        fconfig.setMultiValued("Year", true);
        fconfig.setMultiValued("Auhtor_Keyword", true);

        app.get("/{text}", ctx -> {
            Query q1;
            String titleParam = ctx.queryParam("title");
            String authorParam = ctx.queryParam("author");
            String keywordParam = ctx.queryParam("keyword");
            String yearParam = ctx.queryParam("year");

            String queryAppend = "Content:" + ctx.pathParam("text");
            if (titleParam != null) {
                queryAppend += " AND Title:" + titleParam;
            }
            if (authorParam != null) {
                queryAppend += " AND Author:" + authorParam;
            }
            // if (keywordParam != null) {
            // queryAppend += " AND Keyword:" + keywordParam;
            // }

            System.out.println(queryAppend);
            q1 = parser.parse(queryAppend);
            // TopDocs topdocs = searcher.search(q1, 20);
            TopDocs topdocs = FacetsCollector.search(searcher, q1, 20, fc);
            ScoreDoc[] hits = topdocs.scoreDocs;

            Facets facetas = new FastTaxonomyFacetCounts(taxoReader, fconfig, fc);
            DrillDownQuery dq = new DrillDownQuery(fconfig, q1);
            if (keywordParam != null || yearParam != null) {
                if (keywordParam != null) {
                    System.out.println(keywordParam);
                    dq.add("Keyword", keywordParam);
                }
                if (yearParam != null) {
                    dq.add("Year", yearParam);
                }
                DrillSideways ds = new DrillSideways(searcher, fconfig, taxoReader);
                DrillSidewaysResult dsresult = ds.search(dq, 20);
                topdocs = dsresult.hits;
                hits = topdocs.scoreDocs;
            }

            List<FacetResult> TodasDims = facetas.getAllDims(5);
            System.out.println("TOTAL: " + TodasDims.size());
            ctx.result(DocsToJSON(hits, TodasDims));
        });
    }
}
