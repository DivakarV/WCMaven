package com.pramati.training.webcrawler.common.exception;

/**
 * @author Divakar Viswanathan
 * @since 1.0
 */
public class InvalidURLException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidURLException(String message) {
		super(message);
	}
	
	public InvalidURLException(String message, Exception cause) {
		super(message, cause);
	}
}
