package com.pramati.training.webcrawler.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Matcher;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.HttpStatusException;
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
	 * 
	 * @param args
	 * @author Divakar Viswanathan
	 * @throws IOException 
	 * @since 1.0
	 */
	public static void main(String[] args) throws IOException {
		LOG.info("Entering WebCrawlerImpl...");
		InputStream fileInput = null;
		try {
			ICrawler webCrawlerImpl = WebCrawlerImpl.getInstance();
			fileInput = WebCrawlerImpl.class.getClassLoader().getResourceAsStream(WebCrawlerConstants.CRAWL_PROPS_FILE_LOC);
			Properties crawlerProperties = new Properties();
			crawlerProperties.load(fileInput);
			String baseURL = crawlerProperties.getProperty(WebCrawlerConstants.CRAWL_URL);
			String criteria = crawlerProperties.getProperty(WebCrawlerConstants.CRAWL_CRITERIA);
			String crawlRepoLocation = crawlerProperties.getProperty(WebCrawlerConstants.CRAWL_REPO_LOCATION);
			if (baseURL == null || baseURL.trim().equals("")) {
				throw new InvalidURLException("Empty URL");
			} else {
				Matcher urlMatcher = WebCrawlerConstants.URL_REGEX.matcher(baseURL);
				if (!urlMatcher.matches()) {
					throw new InvalidURLException("Invalid URL Pattern");
				}
			}
			if (crawlRepoLocation == null || crawlRepoLocation.trim().equals("")) {
				LOG.info("Crawl Location is empty. Setting it to default location crawl/root/");
				crawlRepoLocation = WebCrawlerConstants.DEF_CRAWL_REPO_LOC;
			}
			File dir = new File(crawlRepoLocation);
			dir.mkdirs();
			CrawlConfigTO crawlConfig = new CrawlConfigTO();
			crawlConfig.setCrawlRepoLocation(crawlRepoLocation);
			crawlConfig.setBaseCrawlLocation(baseURL);
			crawlConfig.setCrawledLocations(new HashSet<String>());
			crawlConfig.setCrawlCriteria(criteria);
			System.out.println("Crawling has begun..");
			webCrawlerImpl.crawlData(baseURL, crawlConfig);
			System.out.println("Crawling has completed successfully..");
		} catch (IOException e) {
			LOG.error("Error occurred", e);
		} catch (InvalidURLException e) {
			LOG.error("Error occurred due to Invalid URL", e);
		} finally {
			fileInput.close();
		}
	}

	@Override
	public void crawlData(String crawlLocation, CrawlConfigTO crawlConfig) throws IOException {
		boolean canCrawl = false;
		if (crawlLocation.equals(crawlConfig.getBaseCrawlLocation())) {
			canCrawl = crawlConfig.getCrawledLocations().add(crawlLocation);
		} else if (crawlConfig.getCrawlCriteria() != null && !crawlConfig.getCrawlCriteria().equals("")) {
			if (crawlLocation.contains(crawlConfig.getCrawlCriteria())) {
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
				doc = Jsoup.connect(crawlLocation).ignoreContentType(true).get();
				archieveContent(doc.text(), crawlLocation.replaceAll(WebCrawlerConstants.ALL_SPL_CHARS, ""),
						crawlConfig);
			} catch (HttpStatusException hste) {
				LOG.debug("Error connecting to the URL " + crawlLocation + "skipping it..", hste);
				return;
			} catch (IOException e1) {
				LOG.error("Error occurred while processing page", e1);
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
		int indexOfSearchCriteria = fileName.indexOf(crawlConfig.getCrawlCriteria());
		StringBuffer crawlRepoLocation = new StringBuffer(crawlConfig.getCrawlRepoLocation());
		if (indexOfSearchCriteria != -1) {
			int ind = indexOfSearchCriteria + crawlConfig.getCrawlCriteria().length();
			String monthID = fileName.substring(ind, ind + WebCrawlerConstants.MONTH_ID_LENGTH);
			Matcher monMatcher = WebCrawlerConstants.MONTH_ID.matcher(monthID);
			if (monMatcher.matches()) {
				crawlRepoLocation.append(File.separator).append(crawlConfig.getCrawlCriteria()).append(File.separator)
						.append(monthID);
				File dir = new File(crawlRepoLocation.toString());
				dir.mkdirs();
			}
		}
		File file = new File(crawlRepoLocation.toString(), fileName.concat(WebCrawlerConstants.TEXT_FILE_EXTN));
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
