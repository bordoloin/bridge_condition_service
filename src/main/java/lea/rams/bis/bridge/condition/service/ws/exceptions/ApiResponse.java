/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.exceptions;

/**
 * @author Nobol
 *
 */
public class ApiResponse {

	private int status; // 1 for success, 0 for error
	private String message;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ApiResponse(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

}
