/*
 * PURPOSE
 *  getting input with PostsDAO and writing output with CountersDAO
 *  the document, which was stored in Posts table, is parsed by KoreanParser
 *  into words that have meanings themselves.
 *  and the permutations of the words are counted in CountersTable
 */

package hbase.counter;

import hbase.dao.CountersDAO;
import hbase.dao.PostsDAO;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.Text;

import parser.KoreanParser;

public class Mapper	extends TableMapper<Text, Put> {

	private KoreanParser parser = new KoreanParser();

	protected void map(ImmutableBytesWritable rowkey, Result result, 
			Context context) throws IOException {
		
		PostsDAO pDao = new PostsDAO();
		CountersDAO cDao = new CountersDAO();		
		
		String doc = pDao.getTitle(result) + " " + pDao.getContent(result);
		
		List<String >words = parser.extractDistinctWords(doc);

		// counting the permutations using a hbase table
		for (String w1 : words) {
			for (String w2 : words) {
				// ignore two same words.
				if (w1.equals(w2)) continue;
				
				cDao.incCount(w1 + ":" + w2, 1);								
			}
		}
	}
}
