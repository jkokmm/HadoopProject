/*
 * PURPOSE
 * 	testing if extractDistinctWords method in KoreanParser class works.
 */

package ztest.koreanParser;

import java.util.List;

import parser.KoreanParser;

public class TestDriver {
	public static void main(String[] args) {
		KoreanParser morphyAnalyzer = new KoreanParser();
		
		List<String> nouns = morphyAnalyzer.extractDistinctWords(
				"데모를 사용할 수 있도록 준비 중에 있습니다. 데모를 사용할 수 있도록 준비 중에 있습니다.");
		
		for (String n : nouns) {
			System.out.println(n);
		}
	}
}
