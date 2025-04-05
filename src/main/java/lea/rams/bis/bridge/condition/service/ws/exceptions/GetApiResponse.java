package lea.rams.bis.bridge.condition.service.ws.exceptions;

/**
 * @author Nobol
 *
 */

public class GetApiResponse<T> {

	private int status;
	private String message;
	private T data;

	// Constructor for response with data
	public GetApiResponse(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	// Constructor for response without data
	public GetApiResponse(int status, String message) {
		this.status = status;
		this.message = message;
		this.data = null;
	}

	// Getters and Setters
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

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
