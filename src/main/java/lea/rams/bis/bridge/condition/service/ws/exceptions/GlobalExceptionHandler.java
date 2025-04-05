/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.exceptions;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import jakarta.servlet.http.HttpServletResponse;

/**
 * @author Nobol
 *
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse> handleCustomException(CustomException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(0, ex.getMessage()));
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<GetApiResponse<List<String>>> handleValidationException(MethodArgumentNotValidException ex) {
		List<String> errors = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());
		log.error("Validation errors: {}", errors);
		GetApiResponse<List<String>> response = new GetApiResponse<>(0, "Validation failed", errors);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(0, "Unexpected error occurred!"));
    }

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<GetApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
		log.error("Invalid argument: {}", ex.getMessage(), ex);
		GetApiResponse<Void> response = new GetApiResponse<>(0, ex.getMessage(), null);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", 0);
		response.put("message", ex.getMessage()); // This will contain something like "Request method 'POST' is not
													// supported"
		return new ResponseEntity<>(response, HttpStatus.METHOD_NOT_ALLOWED); // HTTP 405 Method Not Allowed
	}

	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<?> handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", 0);
		response.put("message", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@ExceptionHandler(MultipartException.class)
	public ResponseEntity<Object> handleMultipartException(MultipartException ex) {
		Map<String, Object> responseBody = new HashMap<>();
		responseBody.put("status", HttpStatus.BAD_REQUEST.value());
		responseBody.put("error", "Multipart Error");
		responseBody.put("message", ex.getMessage());
		return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException ex) {		
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("status", 0);
		response.put("message", ex.getParameterName()+ " parameter is missing");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        String errorMessage = "Required part '" + ex.getRequestPartName() + "' is not present.";
        log.warn(errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(0, errorMessage));
    }

}
