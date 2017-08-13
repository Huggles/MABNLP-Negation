import edu.stanford.nlp.pipeline.*;


import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.tokensregex.*;

import edu.stanford.nlp.util.*;




import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import java.util.regex.*;
import java.util.regex.Pattern;

public class NLPClinicalExtraction {

    public static void main(String[] args) {

        
        
        String text = args[0];        
        text = String.join("\"",text.split("..--.."));
        
        Env env = TokenSequencePattern.getNewEnv();
        env.setDefaultStringMatchFlags(NodePattern.CASE_INSENSITIVE);
        env.setDefaultStringPatternFlags(Pattern.CASE_INSENSITIVE);
        CoreMapExpressionExtractor extractor = new CoreMapExpressionExtractor();
        try {
            extractor = CoreMapExpressionExtractor.createExtractorFromFile(env, "clinicalRules.rules");
        } 
        catch(RuntimeException e){
            System.out.println(e.getCause().getLocalizedMessage());
            return;
        } 
         

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit");
        //props.setProperty("regexner.mapping", "org/foo/resources/jg-regexner.txt");
        

        
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        Annotation document = new Annotation(text);   
        pipeline.annotate(document);
        
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);
        if (tokens == null || sentences == null || tokens.size() == 0 || sentences.size() == 0) {
            return;
        }
        for (CoreMap sentence:sentences) {
            
                List<CoreMap> matched = extractor.extractCoreMapsMergedWithTokens(sentence);
                //System.out.println("Line Matched");
                for (CoreMap token:matched) {
                    
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                      String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                      String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                      String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                      //System.out.println("  Matched token: " + "word="+word + ", lemma="+lemma + ", pos=" + pos + ", ne=" + ne);
            }
        }

        StringWriter sw = new StringWriter();        
        try{
            pipeline.jsonPrint(document,sw);
        }catch(IOException e){
            e.printStackTrace();
        }
        String s = sw.toString();
        System.out.println(s);
        

    }

}
