package crawler;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;

public class HBaseWriter {
	private static String TableName;
	private static Configuration conf = null; 
	private static HConnection hConnection = null;
	
	public HBaseWriter(String tableName) throws IOException {
		HBaseWriter.TableName = tableName;
		
		if (conf == null)
			conf = HBaseConfiguration.create();
		if (hConnection == null)
			hConnection = HConnectionManager.createConnection(conf);
	}

	public void write(Put p) throws IOException {
		HTableInterface ht = hConnection.getTable(TableName);		
		ht.put(p);
		ht.close();
	}
}
