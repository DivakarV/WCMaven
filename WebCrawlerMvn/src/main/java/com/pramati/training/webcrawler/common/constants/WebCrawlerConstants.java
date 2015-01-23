/**
 * Copyright Company, Inc. 2015
 */
package com.pramati.training.webcrawler.common.constants;

import java.util.regex.Pattern;


/**
 *
 * @author Divakar Viswanathan
 * @since 1.0
 */
public class WebCrawlerConstants {
	
	public final static String ALL_SPL_CHARS = "[^a-zA-Z0-9]";
	
	public final static String ANCHOR_TABS = "a[href]";
	
	public final static String ABS_LINKS = "abs:href";
	
	public final static String TEXT_FILE_EXTN = ".txt";
	
	public final static String DEF_CRAWL_REPO_LOC = "crawl/root/";
	
	public final static Pattern URL_REGEX = Pattern.compile("(http(s)?://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?\\+\\%/\\.\\w]+)?");
	
	public final static Pattern MONTH_ID = Pattern.compile("^([0][1-9]|[1][012])$");
	
	public final static int MONTH_ID_LENGTH = 2;
			
}
