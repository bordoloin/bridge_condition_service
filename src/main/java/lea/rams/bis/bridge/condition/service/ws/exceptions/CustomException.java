/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.exceptions;

/**
 * @author Nobol
 *
 */
public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomException(String message) {
		super(message);
	}

	public CustomException(String message, Throwable cause) {
		super(message, cause);
	}

}
