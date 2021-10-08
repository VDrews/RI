package P2;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.*;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.*;

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
    System.out.println(files.length);
    // 1. Comparar Analizadores
    for (File f : files) {

      Analyzer whiteAnalizer = new WhitespaceAnalyzer();
      Analyzer simpleAnalizer = new SimpleAnalyzer();
      Analyzer stopAnalizer = new StopAnalyzer();
      Analyzer standardAnalizer = new StandardAnalyzer();
      Analyzer spanishAnalizer = new SpanishAnalyzer();

      DocumentAnalyzer doc = new DocumentAnalyzer(f);

      tokenizeString(whiteAnalizer, doc.getContenido());
      tokenizeString(simpleAnalizer, doc.getContenido());
      tokenizeString(stopAnalizer, doc.getContenido());
      tokenizeString(standardAnalizer, doc.getContenido());
      tokenizeString(spanishAnalizer, doc.getContenido());
    }
  }

  public static void tokenizeString(Analyzer analyzer, String content) {
    try {
      TokenStream stream = analyzer.tokenStream(null, new StringReader(content));
      CharTermAttribute cAtt = stream.getAttribute(CharTermAttribute.class);
      TermFrequencyAttribute tFreq = stream.getAttribute(TermFrequencyAttribute.class);

      stream.reset();
      while (stream.incrementToken()) {
        System.out.println(cAtt.toString() + " : " + tFreq.getTermFrequency());
      }
      stream.end();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
