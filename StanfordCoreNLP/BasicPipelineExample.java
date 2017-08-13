import edu.stanford.nlp.pipeline.*;
import java.util.*;
import java.io.*;

public class BasicPipelineExample {

    public static void main(String[] args) {

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        
        
        String text = args[0];
        
        text = String.join("\"",text.split("..--.."));


        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
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
