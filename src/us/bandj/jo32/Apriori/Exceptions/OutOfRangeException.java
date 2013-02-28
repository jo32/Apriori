package us.bandj.jo32.Apriori.Exceptions;

@SuppressWarnings("serial")
public class OutOfRangeException extends Exception {
	
	public OutOfRangeException(String message) {
		super(message);
	}

	public OutOfRangeException(String message, Throwable throwable) {
		super(message, throwable);
	}
	
}
