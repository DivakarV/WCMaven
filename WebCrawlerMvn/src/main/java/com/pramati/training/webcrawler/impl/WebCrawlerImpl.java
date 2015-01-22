package com.pramati.training.webcrawler.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.pramati.training.crawler.common.ICrawler;
import com.pramati.training.webcrawler.common.constants.WebCrawlerConstants;
import com.pramati.training.webcrawler.common.exception.InvalidURLException;
import com.pramati.training.webcrawler.to.CrawlConfigTO;

/**
 *
 * @author Divakar Viswanathan
 * @since 1.0
 */
public final class WebCrawlerImpl implements ICrawler {

	private static WebCrawlerImpl instance;

	private final static Logger LOG = (Logger) LogManager.getLogger(WebCrawlerImpl.class);

	private WebCrawlerImpl() {

	}

	/**
	 * @return
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public static WebCrawlerImpl getInstance() {
		if (instance == null) {
			synchronized (WebCrawlerImpl.class) {
				if (instance == null) {
					instance = new WebCrawlerImpl();
				}
			}
		}
		return instance;
	}

	/**
	 * Entry point for the program
	 * @param args
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public static void main(String[] args) {
		LOG.info("Entering WebCrawlerImpl...");
		Scanner in = null;
		try {
			in = new Scanner(System.in);
			ICrawler webCrawlerImpl = WebCrawlerImpl.getInstance();
			String baseURL;
			String criteria;
			String crawlRepoLocation;
			System.out.println("Enter the url to be crawled:");
			baseURL = in.nextLine();
			if(baseURL == null || baseURL.trim().equals("")) {
				throw new InvalidURLException("Empty URL");
			}
			System.out.println("Enter the repo location for crawled content (Sample: crawl/root/):");
			crawlRepoLocation = in.nextLine();
			if(crawlRepoLocation == null || crawlRepoLocation.trim().equals("")) {
				LOG.info("Crawl Location is empty. Setting it to default location crawl/root/");
				crawlRepoLocation = WebCrawlerConstants.DEF_CRAWL_REPO_LOC;
			}
			System.out.println("Enter the year for crawling (Sample: 2014) : ");
			criteria = in.nextLine();
			File dir = new File(crawlRepoLocation);
			dir.mkdirs();
			// Validate URL, if invalid throw an invalid url exception
			CrawlConfigTO crawlConfig = new CrawlConfigTO();
			crawlConfig.setCrawlRepoLocation(crawlRepoLocation);
			crawlConfig.setBaseCrawlLocation(baseURL);
			crawlConfig.setCrawledLocations(new HashSet<String>());
			crawlConfig.setCrawlCriteria(criteria);
			webCrawlerImpl.crawlData(baseURL, crawlConfig);
			System.out.println("Crawling has been done successfully..");
		} catch (IOException e) {
			LOG.error("Error occurred", e);
		} catch (InvalidURLException e) {
			LOG.error("Error occurred due to Invalid URL", e);
		} finally {
			in.close();
		}
	}

	@Override
	public void crawlData(String crawlLocation, CrawlConfigTO crawlConfig) throws IOException {
		boolean canCrawl = false;
		if (crawlLocation.equals(crawlConfig.getBaseCrawlLocation())) {
			canCrawl = crawlConfig.getCrawledLocations().add(crawlLocation);
		} else if (crawlConfig.getCrawlCriteria() != null && !crawlConfig.getCrawlCriteria().equals("")) {
			if(crawlLocation.contains(crawlConfig.getCrawlCriteria())) {
				canCrawl = crawlConfig.getCrawledLocations().add(crawlLocation);
			} else {
				return;
			}
		} else if (crawlConfig.getCrawlCriteria() == null || crawlConfig.getCrawlCriteria().equals("")) {
			canCrawl = crawlConfig.getCrawledLocations().add(crawlLocation);
		} else {
			return;
		}
		if (canCrawl) {
			Document doc = null;
			try {
				doc = Jsoup.connect(crawlLocation).get();
				archieveContent(doc.text(), crawlLocation.replaceAll(WebCrawlerConstants.ALL_SPL_CHARS, ""), crawlConfig);
			} catch (IOException e1) {
				LOG.debug("Error occurred while processing page", e1);
				throw e1;
			}
			Elements links = doc.select(WebCrawlerConstants.ANCHOR_TABS);
			for (Element link : links) {
				crawlData(link.attr(WebCrawlerConstants.ABS_LINKS), crawlConfig);
			}
		}
	}

	@Override
	public void archieveContent(String textContent, String fileName, CrawlConfigTO crawlConfig) throws IOException {
		File file = new File(crawlConfig.getCrawlRepoLocation(), fileName.concat(WebCrawlerConstants.TEXT_FILE_EXTN));
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
			fileWriter.write(textContent);
		} catch (IOException e) {
			LOG.error("Error occurred while writing to files", e);
			throw e;
		} finally {
			try {
				fileWriter.close();
			} catch (IOException e) {
				LOG.error("Error occurred while closing writer", e);
			}
		}
	}
}
