/*
 * PUPOSE
 *  parsing html into whatever form user want.
 *  it has bunch of methods starting with 'get'
 *  to extract information from htmls.
 *  
 *  and only another form the program need is 'Put' class for hbase.
 *  so, it provides 'mkPut' which extract information from html and
 *  make put for hbase.
 */

package parser;

import hbase.dao.PostsDAO;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.sun.el.parser.ParseException;

public class HTMLParser {
	public static final byte[] POSTS_FAM = Bytes.toBytes("p");
	public static final byte[] TITLE_COL = Bytes.toBytes("title");
	public static final byte[] CONTENT_COL = Bytes.toBytes("content");	

	public Put mkPut(String html) throws ParseException {
		Document doc = Jsoup.parse(html, "UTF-8");
		
		// checking validation
		if (isDeleted(doc)) throw new ParseException("page has been deleted.");
		if (!hasIndex(doc)) throw new ParseException("index cannot be found.");
		
		Put p = new Put(Bytes.toBytes(getIndex(doc)));		
		p.add(PostsDAO.INPUT_FAM, PostsDAO.INPUT_TITLECOL, Bytes.toBytes(getTitle(doc)));
		p.add(PostsDAO.INPUT_FAM, PostsDAO.INPUT_CONTENTCOL, Bytes.toBytes(getContent(doc)));
		
		/* debugging lines
		System.out.println("index : " + getIndex(doc));
		System.out.println("title : " + getTitle(doc));
		System.out.println("cntnt : " + getContent(doc));
		System.out.println("date  : " + getDate(doc));
		*/
		
		return p;
	}
	
	private boolean isDeleted(Document doc) {
		if (doc.title() == "일베저장소 - 일간베스트, 개드립, 짤방, 플래시 게임, 유머 게시판" ||
			doc.getElementsByClass("tCenter messageBox").text() == "삭제된 글입니다.")			
			return true;
		return false;
	}
	
	private boolean hasIndex(Document doc) {
		return getIndex(doc) == null? false : true;
	}
	
	// get methods extract information from html documents.
	private String getTitle(Document doc){
		String title = doc.title();
		int endIndex = title.length() - 21;
		return title.substring(0, endIndex);
	}
	
	private String getContent(Document doc) {
		String content = doc.getElementsByClass("contentBody").text();
		
		if (content.endsWith(" 이 게시물을...")) {
			int endIndex = content.length() - " 이 게시물을...".length();
			return content.substring(0, endIndex);
		}
		return content;
	}
	
	private String getDate(Document doc) {
		String date = doc.getElementsByClass("date").text();
		if (date.length() > 10)
			return date.substring(0, 10);
		return "";
	}
	
	private String getIndex(Document doc) {
		String str = doc.getElementsByClass("dateAndCount").text();
		String url = str.split(" ")[0];
		String[] urlSplit = url.split("/");
		
		if (urlSplit.length >= 3)
			return url.split("/")[3];
		return null;
	}
}
