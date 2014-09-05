/*
 * PURPOSE
 *  providing the interface to posts table.
 *  'ilbePosts' table has one column family, 'p', and
 *  two qualifier, 'content' and 'title'.
 */

package hbase.dao;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class PostsDAO {
	public static byte[] INPUT_TABLE = Bytes.toBytes("ilbe");
	public static byte[] INPUT_FAM = Bytes.toBytes("p");
	public static byte[] INPUT_CONTENTCOL = Bytes.toBytes("content");
	public static byte[] INPUT_TITLECOL = Bytes.toBytes("title");
	
	private Configuration conf = HBaseConfiguration.create();
	private HConnection connection = HConnectionManager.createConnection(conf);
	
	public PostsDAO() throws IOException { }
	
	public String getContent(Result r) {		
		return Bytes.toString(getCell(r, INPUT_CONTENTCOL));
	}
	
	public String getTitle(Result r) {
		return Bytes.toString(getCell(r, INPUT_TITLECOL));
	}
	
	private byte[] getCell(Result r, byte[] qualifier) {
		return CellUtil.cloneValue(r.getColumnLatestCell(INPUT_FAM, qualifier));
	}

	public void put(Put p) throws IOException {
		HTableInterface t = connection.getTable(INPUT_TABLE);		
		t.put(p);
		t.close();
	}
}
