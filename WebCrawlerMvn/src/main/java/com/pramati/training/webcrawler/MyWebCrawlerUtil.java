package com.pramati.training.webcrawler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class that crawls web pages
 * @author Divakar Viswanathan
 * @since 1.0
 */
public class MyWebCrawlerUtil {

	private static Set<String> visitedURLs= new HashSet<String>();
	
	private final static Logger LOG = (Logger) LogManager.getLogger(MyWebCrawlerUtil.class);

	private final static String BASE_URL = "http://mail-archives.apache.org/mod_mbox/maven-users/";

	private static int pagesCrawled = 0;
	
	private final static int MAX_PAGE_COUNT = 10;
	
	private final static String URL_CRITERIA = "2014";

	/**
	 * Entry Point for the Util
	 * @param args
	 * @throws IOException
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public static void main(String[] args) throws IOException {
		processPage(BASE_URL);
	}

	/**
	 * Method to process the web pages
	 * @param URL
	 * @throws IOException
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public static void processPage(String currentURL) throws IOException {
		boolean canProcess = false;
		if (pagesCrawled == MAX_PAGE_COUNT) {
			return;
		}
		if (currentURL.equals(BASE_URL)) {
			canProcess = visitedURLs.add(currentURL);
		} else if (currentURL.contains(URL_CRITERIA)) {
			canProcess = visitedURLs.add(currentURL);
		} else {
			return;
		}
		if (canProcess) {
			pagesCrawled++;
			System.out.println("Processed URL:  " + currentURL);
			Document doc = null;
			try {
				doc = Jsoup.connect(currentURL).get();
			} catch (IOException e1) {
				LOG.debug("Error occurred while processing page", e1);
				throw e1;
			}
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				processPage(link.attr("abs:href"));
			}
		}
	}
}