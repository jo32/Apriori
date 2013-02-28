package us.bandj.jo32.Apriori.Exceptions;

@SuppressWarnings("serial")
public class NoRequiredItemSetException extends Exception {

	public NoRequiredItemSetException(String message) {
		super(message);
	}

	public NoRequiredItemSetException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
