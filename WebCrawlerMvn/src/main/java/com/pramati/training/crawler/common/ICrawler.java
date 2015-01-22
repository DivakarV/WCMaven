/**
 * Copyright Company, Inc. 2015
 */
package com.pramati.training.crawler.common;

import java.io.IOException;

import com.pramati.training.webcrawler.to.CrawlConfigTO;

/**
 *
 * @author Divakar Viswanathan
 * @since 1.0
 */
public interface ICrawler {
	
	/**
	 * 
	 * @author Divakar Viswanathan
	 * @throws IOException 
	 * @since 1.0
	 */
	public void crawlData(String crawlLocation, CrawlConfigTO crawlConfig) throws IOException;
	
	/**
	 * 
	 * @author Divakar Viswanathan
	 * @throws IOException 
	 * @since 1.0
	 */
	public void archieveContent(String textContent, String fileName, CrawlConfigTO crawlConfig) throws IOException;

}
