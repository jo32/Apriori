package us.bandj.jo32.Apriori.Exceptions;

@SuppressWarnings("serial")
public class InvalidRuleException extends Exception {
	public InvalidRuleException(String message) {
		super(message);
	}

	public InvalidRuleException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
