package com.pramati.training.webcrawler.to;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Divakar Viswanathan
 * @since 1.0
 */
public class CrawlConfigTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String crawlRepoLocation;
	
	private String baseCrawlLocation;
	
	private Set<String> crawledLocations;
	
	private String crawlCriteria;
	
	/**
	 * @return the crawlRepoLocation
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public String getCrawlRepoLocation() {
		return crawlRepoLocation;
	}

	/**
	 * @param crawlRepoLocation the crawlRepoLocation to set
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public void setCrawlRepoLocation(String crawlRepoLocation) {
		this.crawlRepoLocation = crawlRepoLocation;
	}

	/**
	 * @return the baseCrawlLocation
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public String getBaseCrawlLocation() {
		return baseCrawlLocation;
	}

	/**
	 * @param baseCrawlLocation the baseCrawlLocation to set
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public void setBaseCrawlLocation(String baseCrawlLocation) {
		this.baseCrawlLocation = baseCrawlLocation;
	}

	/**
	 * @return the crawledLocations
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public Set<String> getCrawledLocations() {
		return crawledLocations;
	}

	/**
	 * @param crawledLocations the crawledLocations to set
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public void setCrawledLocations(Set<String> crawledLocations) {
		this.crawledLocations = crawledLocations;
	}

	/**
	 * @return the crawlCriteria
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public String getCrawlCriteria() {
		return crawlCriteria;
	}

	/**
	 * @param crawlCriteria the crawlCriteria to set
	 * @author Divakar Viswanathan
	 * @since 1.0
	 */
	public void setCrawlCriteria(String crawlCriteria) {
		this.crawlCriteria = crawlCriteria;
	}
}
