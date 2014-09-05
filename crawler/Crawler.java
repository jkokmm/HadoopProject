/*
 * PURPOSE
 *  crawling the website.
 *  processing and storing the htmls in hbase.
 */

package crawler;

import hbase.dao.PostsDAO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import parser.HTMLParser;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {
	private final static String BaseURL = "http://www.ilbe.com/";
	private final static String ExceptionURLDumpPathFMT = "/home/jinhoim/ilbe/unparsed/%d.html";
	
	private HTMLParser HtmlParser = new HTMLParser();
	private PostsDAO pDao = new PostsDAO();
	
	public Crawler() throws IOException {}

	/**
	 * You should implement this function to specify whether
	 * the given url should be crawled or not (based on your
	 * crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		String href = url.getURL().toLowerCase();                
		String index = href.substring(BaseURL.length(), href.length());
		
		return href.startsWith("http://www.ilbe.com/")
			   && index.matches("-?\\d+(\\.\\d+)?");
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		if (page.getWebURL().toString().equals(BaseURL)) return;
		
        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String html = htmlParseData.getHtml();
            
            try {
            	pDao.put(HtmlParser.mkPut(html));
            	
            	//System.out.print(this.getThread().getId() + " crawled " + 
            	//page.getWebURL().toString());
			} catch (Exception e) {
				String url = page.getWebURL().toString();
				try {
					BufferedWriter bw;
					String path = String.format(ExceptionURLDumpPathFMT, url);
					
					bw = new BufferedWriter(new FileWriter(path));
					bw.write(html);
					bw.flush();
					bw.close();
				} catch (IOException ioe) {
					// TODO Auto-generated catch block
					ioe.printStackTrace();
				}
			}
        }
	}
}
