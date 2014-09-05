package parser;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class KoreanParser { 
	public Komoran Analyzer; 
	
	public KoreanParser() {
		Analyzer = new Komoran("/home/jinhoim/java/komoran-2.1.2-e/models/");
	}
	
	public List<String> extractDistinctWords(String doc) {
		List<String> nouns = new LinkedList<String>();
		Set<String> repetitionChecker = new HashSet<String>();
		
		List<List<Pair<String, String>>> result = Analyzer.analyze(doc);		
		
		for (List<Pair<String, String>> eojeolResult : result) {
			for (Pair<String, String> wordMorph : eojeolResult) {
				
				String word = wordMorph.getFirst();
				String type = wordMorph.getSecond();
				
				if (type.equals("NNG") && !repetitionChecker.contains(word)) {
					repetitionChecker.add(word);
					nouns.add(word);
				}
			}
		}
		
		return nouns;
	}
}
