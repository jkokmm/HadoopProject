/*
 * PURPOSE
 * 	getting a source from input table, parsing into words,
 *  making the permutations, and counting them by storing in another table.
 */
package ztest.counter;

import hbase.dao.CountersDAO;

import java.io.IOException;

import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;

public class TestMapper extends TableMapper<Text, Put>{

	protected void map(ImmutableBytesWritable rowkey, Result result, 
			Context context) {
		// appending p:title and p:content in the result argumented. 
		// str = "<title> <content>"
		String str = "";
		str += Bytes.toString(CellUtil.cloneValue(
				result.getColumnLatestCell("p".getBytes(), "title".getBytes())));
		str += " ";
		str += Bytes.toString(CellUtil.cloneValue(
				result.getColumnLatestCell("p".getBytes(), "content".getBytes())));
		
		// to make the permutations
		String[] alphas = str.split(" ");
		
		/* debug printing
		for (String s : alphas)
			System.out.println(s);
		*/
		
		// making the permutations, which increase count in the output table.
		// thus, we can figure out how many times each permutation has been made.
		for (String s1 : alphas) {
			for (String s2 : alphas) {
				if (s1.equals(s2)) continue;
				
				String outkey = s1 + ":" + s2;
				
				try {
					// delegate increasing count to CounterDAO class
					CountersDAO cDao = new CountersDAO();
					cDao.incCount(outkey, 1);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
}
