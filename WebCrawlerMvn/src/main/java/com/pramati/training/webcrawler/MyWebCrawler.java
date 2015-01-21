package com.pramati.training.webcrawler;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Class that crawls web pages
 * @author Divakar Viswanathan
 * @since 1.0
 */
public class MyWebCrawler {
	public static int counter = 10;
	public static String baseURL = "http://mail-archives.apache.org/mod_mbox/maven-users/"; 
	private final static Logger LOG = LogManager.getLogger(MyWebCrawler.class); 
	public static void main(String[] args) {
		processPage(baseURL);
	}

	/**
	 * Method to process the pages
	 * @param URL
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public static void processPage(String URL) {
		Document doc;
		try {
			if(counter > 0) {
				counter--;
			} else {
				return;
			}
			doc = Jsoup.connect(URL).get();
			System.out.println(doc.text());

			// get all links and recursively call the processPage method
			Elements questions = doc.select("a[href]");
			for (Element link : questions) {
				if (link.attr("href").contains("2014"))
					processPage(link.attr("abs:href"));
			}
		} catch (IOException e) {
			LOG.error("Exception while processing page", e);
		}
	}
}
