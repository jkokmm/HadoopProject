/*
 * PURPOSE
 *  configurating a job for mapreduce.
 *  but, it only uses a mapper.
 */

package driver;

import hbase.counter.Mapper;
import hbase.dao.PostsDAO;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
import org.apache.hadoop.mapreduce.Job;

public class CounterDriver {	
	public static void main(String[] args) throws Exception {
		
		long start = System.currentTimeMillis();
		Configuration conf = HBaseConfiguration.create();
		Job job = Job.getInstance(conf, "Ilbe Counter");
		job.setJarByClass(CounterDriver.class);

		Scan scan = new Scan();
		scan.addColumn(PostsDAO.INPUT_FAM, PostsDAO.INPUT_TITLECOL);
		scan.addColumn(PostsDAO.INPUT_FAM, PostsDAO.INPUT_CONTENTCOL);

		TableMapReduceUtil.initTableMapperJob(
				Bytes.toString(PostsDAO.INPUT_TABLE),
				scan,
				Mapper.class,
				Text.class,
				Put.class,
				job);

		job.setOutputFormatClass(NullOutputFormat.class);
		
		/*
		TableMapReduceUtil.initTableReducerJob(
				Bytes.toString(CountersDAO.OUTPUT_TESTTABLE),
				Reducer.class,
				job);
		*/
		
		job.setNumReduceTasks(0);
		boolean result = job.waitForCompletion(true);  
		System.out.println("the program takes " 
						   + (start - System.currentTimeMillis())
						   + "milliseconds");
		System.exit(result ? 0 : 1);
	}
}
