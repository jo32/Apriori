package us.bandj.jo32.Apriori.Exceptions;

@SuppressWarnings("serial")
public class InvalidInputArgumentsException extends Exception {
	public InvalidInputArgumentsException(String message) {
		super(message);
	}

	public InvalidInputArgumentsException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
