/*
 * PURPOSE
 * 	the driver for test mapreduce.
 * 	executing a series of steps for mapreduce. 
 */

package ztest.counter;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;

public class TestCounterDriver {
	public static void main(String[] args) throws Exception {
		
		// steps for executing mapreduce
		Job job = Job.getInstance(HBaseConfiguration.create(), 
								  "Ilbe Counter Test");
		job.setJarByClass(TestCounterDriver.class);

		Scan scan = new Scan();
		scan.addColumn("p".getBytes(), "title".getBytes());
		scan.addColumn("p".getBytes(), "content".getBytes());

		TableMapReduceUtil.initTableMapperJob(
				Bytes.toString("counterTestInput".getBytes()),
				scan,
				ztest.counter.TestMapper.class,
				Text.class,
				Put.class,
				job);
		
		// not using a reducer.
		job.setOutputFormatClass(NullOutputFormat.class);

/*		
		TableMapReduceUtil.initTableReducerJob(
				Bytes.toString("counterTestOutput".getBytes()),
				counter.test.Reducer.class,
				job);
*/
		job.setNumReduceTasks(0);
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
