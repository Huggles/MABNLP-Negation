import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.*;
import java.util.regex.Pattern;
import java.lang.*;


 
public class CallKit{
	public static void main(String[] args) {
		

	  try {
	  	
		if (args.length != 2) {
			System.out.println("Usage: java CallKit path.to.negex.trigger.terms path.to.file.with.sentences.to.test");
			System.exit(-1);
		}
		

		GenNegEx g 				= new GenNegEx();
		String fillerString		= "_";
		boolean negatePossible	= true;

		String triggersFile		= "negex/negex_triggers.txt";
		String sentencesFile	= args[0];
		String termsFilePath	= args[1];		

        File ruleFile           = new File(triggersFile);
		File testKitFile		= new File(sentencesFile);
		File termsFile			= new File(termsFilePath);

        Scanner sc              = new Scanner(ruleFile);
		Scanner scKit			= new Scanner(testKitFile);
		Scanner scTerms 		= new Scanner(termsFile);
		String termsString 		= scTerms.useDelimiter("\\A").next();
		String textString		= scKit.useDelimiter("\\A").next();
		String[] lines 		 	= textString.split("\\n");
		

		
		ArrayList rules			= new ArrayList();
		String afterNegCheck		= "";



		while (sc.hasNextLine()) {
			rules.add(sc.nextLine());
		}
		
		try {		

			for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
				Pattern pSplit		= Pattern.compile("[\\t]+");
				String line 		= lines[lineNumber];
				
				

				String[] split = termsString.split(",");
				for (int i = 0; i < split.length; i++) {
					String phrase = split[i];
								

					afterNegCheck = g.negCheck(line, phrase, rules, negatePossible);
					System.out.println(afterNegCheck);
					

					String[] content = pSplit.split(afterNegCheck);
					if(content[1].contains("negated")){
						System.out.println(phrase);						
					}
					

				}
			}
			
		}
		catch (Exception e) {
			
			System.out.println(e);
		}
		sc.close();
		scKit.close();
		scTerms.close();
	  }
	  catch (Exception e) {
	  	
		System.out.println(e.getMessage());
		e.printStackTrace();
	  }
	}
}
