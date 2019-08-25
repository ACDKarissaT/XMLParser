package xml.exceptions;


/**
 * <p>
 * This exception class is for XML format errors.
 * </p>
 * @author Karissa Tuason
 * @since 1.0
 */
public class InvalidXMLFormatException extends Exception{

	/**
	 * serial version.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor. Creates an InvalidXMLFormatException with "Invalid xml format" as its message.
	 */
	public InvalidXMLFormatException() {
		this("Invalid xml format.");
	}
	
	/**
	 * Constructor. Creates InvalidXMLFormatException with given message.
	 * @param message exception message
	 */
	public InvalidXMLFormatException(String message) {
		super(message);
	}
	
}
