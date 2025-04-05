/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import lea.rams.bis.bridge.condition.service.ws.dtos.BridgeConditionDTO;
import lea.rams.bis.bridge.condition.service.ws.dtos.BridgeConditionResponseDTO;
import lea.rams.bis.bridge.condition.service.ws.dtos.ConditionSurveyDateListResponseDTO;
import lea.rams.bis.bridge.condition.service.ws.exceptions.CustomException;
import lea.rams.bis.bridge.condition.service.ws.services.BridgeConditionService;

/**
 * @author Nobol
 *
 */

@RestController
@RequestMapping("/lea/rams/bridge-condition")
public class BridgeConditionController {
	
	private static final Logger log = LoggerFactory.getLogger(BridgeConditionController.class);
	
	private final BridgeConditionService bridgeConditionService;

	public BridgeConditionController(BridgeConditionService bridgeConditionService) {
		this.bridgeConditionService = bridgeConditionService;
	}
	
	@PostMapping
	public ResponseEntity<Map<String, Object>> insertBridgeConditions(@RequestParam(required = true) Long bridgeId, @RequestBody List<BridgeConditionDTO> conditionDTOs) {
		Map<String, Object> responseMap = new HashMap<>();
		Long resBridgeId = null;
		try {
			log.debug("Received request to insert bridge conditions. Bridge ID: {}, Conditions: {}", bridgeId, conditionDTOs);
			resBridgeId = bridgeConditionService.saveBridgeConditions(bridgeId, conditionDTOs);
			log.info("Bridge conditions inserted successfully. Bridge ID: {}, Response Bridge ID: {}", bridgeId, resBridgeId);
			responseMap.put("status", "success");
			responseMap.put("message", "Bridge condition successfully saved for the bridge ID: " + resBridgeId);
			log.debug("Response map: {}", responseMap);
			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			log.error("Invalid argument provided while inserting bridge conditions. Bridge ID: {}, Error: {}", bridgeId, e.getMessage());
			responseMap.put("status", "error");
			responseMap.put("message", "Invalid argument: " + e.getMessage());
			log.debug("Response map: {}", responseMap);
			return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			log.error("Unexpected error occurred while inserting bridge conditions. Bridge ID: {}, Error: ", bridgeId, e);
			responseMap.put("status", "error");
			responseMap.put("message", "Internal Server Error");
			log.debug("Response map: {}", responseMap);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			log.debug("Completed request to insert bridge conditions. Bridge ID: {}", bridgeId);
		}
	}
	
	@PutMapping
    public ResponseEntity<Map<String, Object>> updateBridgeConditions(@RequestParam Long bridgeId, @RequestParam Long setId,
            @RequestParam String userId, @RequestBody List<BridgeConditionDTO> conditionDTOs) {
        log.debug("Received request to update bridge conditions. Bridge ID: {}, Set ID: {}, User ID: {}, Total Conditions: {}", bridgeId, setId, userId, conditionDTOs.size());
        try {
            Long updatedBridgeId = bridgeConditionService.updateBridgeConditions(bridgeId, setId, conditionDTOs, userId);
            log.info("Bridge conditions updated successfully. Bridge ID: {}, Set ID: {}", bridgeId, setId);
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Bridge conditions successfully updated for Bridge ID: " + updatedBridgeId
            ));
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument error while updating bridge conditions. Bridge ID: {}, Set ID: {}, Error: {}", bridgeId, setId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "status", "error",
                "message", "Invalid argument: " + e.getMessage()
            ));
        } catch (RuntimeException e) {
            log.error("Runtime exception occurred. Bridge ID: {}, Set ID: {}, Error: {}", bridgeId, setId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", "Internal Server Error: " + e.getMessage()
            ));
        } catch (Exception e) {
            log.error("Unexpected error occurred. Bridge ID: {}, Set ID: {}", bridgeId, setId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "error",
                "message", "An unexpected error occurred. Please try again later."
            ));
        }
    }
	
	@GetMapping("/survey-dates")
	public ResponseEntity<Map<String, Object>> getSurveyDates(@RequestParam(required = true) Long bridgeId) {
		log.info("Fetching condition survey dates for bridgeId: {}", bridgeId);
		Map<String, Object> responseMap = new HashMap<>();
		try {
			List<ConditionSurveyDateListResponseDTO> surveyDates = bridgeConditionService.getSurveyDatesByBridgeId(bridgeId);
			responseMap.put("status", "success");
			responseMap.put("data", surveyDates);
			log.debug("Survey dates retrieved successfully for the bridge ID: " + bridgeId);
			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (CustomException e) {
			responseMap.put("status", "error");
			responseMap.put("message", "Bridge not found with ID: " + bridgeId);
			log.error("BridgeNotFoundException: {}", e.getMessage());
			return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			responseMap.put("status", "error");
			responseMap.put("message", "An error occurred while retrieving survey dates for the bridge ID: " + bridgeId);
			log.error("Exception: {}", e.getMessage(), e);
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	// Method to get distinct components based on setId
	@GetMapping("/components")
	public ResponseEntity<?> getDistinctComponents(@RequestParam(required = true) @Min(1) Long setId) {
		log.info("Received request to get distinct components for setId: {}", setId);
		Map<String, Object> responseMap = new LinkedHashMap<>();
		try {
			// Call the service to fetch components based on setId
			List<String> components = bridgeConditionService.getComponentsBySetId(setId);
			// Check if the result is empty
			if (components.isEmpty()) {
				log.warn("No distinct components found for setId: {}", setId);
				responseMap.put("status", "success");
				responseMap.put("message", "No components found for the provided setId.");
				return new ResponseEntity<>(responseMap, HttpStatus.OK);
			}
			// Log the successful response
			log.info("Successfully fetched {} distinct components for setId: {}", components.size(), setId);
			// Prepare the response
			responseMap.put("status", "success");
			responseMap.put("data", components);
			return new ResponseEntity<>(responseMap, HttpStatus.OK);
		} catch (CustomException e) {
			log.error("Error while processing request to get distinct components for setId: {}: {}", setId, e.getMessage(), e);
			responseMap.put("status", "error");
			responseMap.put("message", e.getMessage());
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (RuntimeException e) {
			log.error("Runtime error while processing request to get distinct components for setId: {}: {}", setId, e.getMessage(), e);
			responseMap.put("status", "error");
			responseMap.put("message", "A runtime error occurred. Please try again later.");
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			log.error("Unexpected error occurred while processing request to get distinct components for setId: {}: {}", setId, e.getMessage(), e);
			responseMap.put("status", "error");
			responseMap.put("message", "An unexpected error occurred. Please try again later.");
			return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
		
	
	@GetMapping("/records/{setId}")
	public ResponseEntity<?> getRecords(@PathVariable Long setId, @RequestParam(required = false) String memberId) {
	    Map<String, Object> responseMap = new LinkedHashMap<>();
	    try {
	        List<BridgeConditionResponseDTO> records = bridgeConditionService.getRecordsBySetIdAndMemberId(setId, memberId);
	        if (records.isEmpty()) {
	            responseMap.put("status", "success");
	            responseMap.put("message", "No records found for the provided setId" + (memberId != null ? " and memberId." : "."));
	            return new ResponseEntity<>(responseMap, HttpStatus.OK);
	        }
	        responseMap.put("status", "success");
	        responseMap.put("data", records);
	        return new ResponseEntity<>(responseMap, HttpStatus.OK);
	    } catch (Exception e) {
	        responseMap.put("status", "error");
	        responseMap.put("message", "An unexpected error occurred. Please try again later.");
	        return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
    
	/**
     * DELETE endpoint to logically delete a bridge condition (by updating is_deleted, lastUpdatedBy,
     * and lastUpdatedAt) based on setId and optionally memberId.
     *
     * @param setId the condition set ID (required)
     * @param memberId the component identifier (optional)
     * @param userId the ID of the user performing the deletion (required)
     * @return ResponseEntity containing a response map with status and message.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteBridgeConditions(@RequestParam("setId") Long setId,
            @RequestParam(value = "memberId", required = false) String memberId, @RequestParam("userId") Long userId) {
        Map<String, Object> responseMap = new HashMap<>();
        try {
            int affectedRows = bridgeConditionService.deleteBridgeConditions(setId, memberId, userId);
            if (affectedRows > 0) {
                responseMap.put("status", "success");
                responseMap.put("message", "Bridge condition deleted successfully. No. of rows deleted: " + affectedRows);
                log.debug("Response map: {}", responseMap);
                return new ResponseEntity<>(responseMap, HttpStatus.OK);
            } else {
                responseMap.put("status", "failure");
                responseMap.put("message", "No bridge condition found with setId " + setId + (memberId != null && !memberId.trim().isEmpty() ? " and memberId " + memberId : ""));
                log.debug("Response map: {}", responseMap);
                return new ResponseEntity<>(responseMap, HttpStatus.NOT_FOUND);
            }
        } catch (Exception ex) {
            log.error("Exception in deleteBridgeConditions controller: {}", ex.getMessage(), ex);
            responseMap.put("status", "error");
            responseMap.put("message", "An error occurred while deleting the bridge condition: " + ex.getMessage());
            return new ResponseEntity<>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/download-report")
    public ResponseEntity<byte[]> generateReport(@RequestParam("userId") String userId, @RequestParam("bridgeId") Long bridgeId, @RequestParam("setId") Long setId) {
        try {
        	String filename = generateFilename("pdf");
			Long userIdLong = Long.parseLong(userId);
            byte[] reportBytes = bridgeConditionService.generateReportBridgeCondition(userIdLong, bridgeId, setId);
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"");
            return ResponseEntity.ok().headers(headers).contentType(org.springframework.http.MediaType.APPLICATION_PDF).body(reportBytes);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    private String generateFilename(String extension) {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		return "BRIDGE_CONDITION_" + timestamp + "." + extension;
	}
    
    @GetMapping("/check")
    public ResponseEntity<Map<String, Object>> checkDataAvailability(@RequestParam Long bridgeId, @RequestParam(required = false) String memberId) {        
        log.info("Checking condition data availability for bridgeId: {} and memberId: {}", bridgeId, memberId);        
        boolean exists = bridgeConditionService.isDataAvailable(bridgeId, memberId);
        Map<String, Object> response = new HashMap<>();
        
        if (exists) {
            response.put("status", "success");
            response.put("message", "Data is available");
            log.info("Data found for bridgeId: {} and memberId: {}", bridgeId, memberId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("status", "not_found");
            response.put("message", "No data found");
            log.warn("No data found for bridgeId: {} and memberId: {}", bridgeId, memberId);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}
