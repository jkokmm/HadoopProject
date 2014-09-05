/*
 * PURPOSE
 *  providing an interface to hbase counter table.
 *  'ilbeCounter' counting table has
 *  one column family, 'c', and one qualifier, 'count'.
 *  
 *  for this table, put command is interchangeable with increment command.
 *  so, only incCount method is provided. 
 */

package hbase.dao;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.util.Bytes;

public class CountersDAO {
	// test purpose 
	public static byte[] OUTPUT_TESTTABLE = Bytes.toBytes("counterTestOutput");
	
	// table
	public static byte[] OUTPUT_TABLE = Bytes.toBytes("ilbeCounter");
	// column family
	public static byte[] OUTPUT_FAM = Bytes.toBytes("c");
	// qualifier
	public static byte[] OUTPUT_COUNTERCOL = Bytes.toBytes("count");
	
	// ready for initiating hbase connection
	private Configuration conf = HBaseConfiguration.create();
	private HConnection connection = HConnectionManager.createConnection(conf);
	
	public CountersDAO() throws IOException {}
	
	/*
	 * PURPOSE
	 *  increasing by the count argumented of the row argumented.
	 */
	public void incCount(String rowkey, int count) throws IOException {
		HTableInterface t = connection.getTable(OUTPUT_TABLE);

		t.incrementColumnValue(Bytes.toBytes(rowkey),
								   OUTPUT_FAM, OUTPUT_COUNTERCOL,
								   count);

		t.close();
	}
}
