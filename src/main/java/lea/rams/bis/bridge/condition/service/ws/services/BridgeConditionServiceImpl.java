/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.services;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.Min;
import lea.rams.bis.bridge.condition.service.ws.dtos.BridgeConditionDTO;
import lea.rams.bis.bridge.condition.service.ws.dtos.BridgeConditionResponseDTO;
import lea.rams.bis.bridge.condition.service.ws.dtos.ConditionSurveyDateListResponseDTO;
import lea.rams.bis.bridge.condition.service.ws.entities.BridgeCondition;
import lea.rams.bis.bridge.condition.service.ws.entities.BridgeConditionView;
import lea.rams.bis.bridge.condition.service.ws.exceptions.CustomException;
import lea.rams.bis.bridge.condition.service.ws.repositories.BridgeConditionRepository;
import lea.rams.bis.bridge.condition.service.ws.repositories.BridgeConditionViewRepository;
import lea.rams.bis.bridge.condition.service.ws.utils.NetworkUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * @author Nobol
 *
 */

@Service
public class BridgeConditionServiceImpl implements BridgeConditionService {
	
	private static final Logger log = LoggerFactory.getLogger(BridgeConditionServiceImpl.class);
	
	private final BridgeConditionRepository bridgeConditionRepository;
	
	private final BridgeConditionViewRepository bridgeConditionViewRepository;

	public BridgeConditionServiceImpl(BridgeConditionRepository bridgeConditionRepository, BridgeConditionViewRepository bridgeConditionViewRepository) {
		this.bridgeConditionRepository = bridgeConditionRepository;
		this.bridgeConditionViewRepository = bridgeConditionViewRepository;
	}
	
	@Autowired
    private EntityManager entityManager;

	@Transactional
	public Long generateSetId() {
		return (Long) entityManager.createNativeQuery("SELECT nextval('bridge_condition_set_id_seq')").getSingleResult();
	}

	@Override
	public Long saveBridgeConditions(Long bridgeId, List<BridgeConditionDTO> conditionDTOs) {
		log.info("Started processing to create BridgeCondition records for {} records", conditionDTOs.size());
		Long setId;
		try {
			setId = generateSetId();
			log.info("Generated set ID: {}", setId);
		} catch (Exception e) {
			log.error("Error generating set ID", e);
			throw new RuntimeException("Failed to generate set ID", e);
		}
		List<BridgeCondition> conditions;
		try {
			conditions = conditionDTOs.stream().map(dto -> {
				BridgeCondition condition = new BridgeCondition();
				condition.setBridgeId(bridgeId);
				condition.setSetId(setId);
				condition.setMemberId(dto.getMemberId());
				condition.setAttributeType(dto.getAttributeType());
				condition.setAttributeValue(dto.getAttributeValue());
				condition.setCreatedAt(LocalDateTime.now().toString());
				return condition;
			}).collect(Collectors.toList());
			log.info("Mapped {} bridge conditions", conditions.size());
		} catch (Exception e) {
			log.error("Error mapping bridge conditions", e);
			throw new RuntimeException("Failed to map bridge conditions", e);
		}
		try {
			bridgeConditionRepository.saveAll(conditions);
			log.info("Saved {} bridge conditions", conditions.size());
		} catch (Exception e) {
			log.error("Error saving bridge conditions", e);
			throw new RuntimeException("Failed to save bridge conditions", e);
		}
		return bridgeId;
	}
	

	@Override
	@Transactional
	public Long updateBridgeConditions(Long bridgeId, Long setId, List<BridgeConditionDTO> conditionDTOs, String userId) {
	    log.info("Started updating BridgeCondition records. Bridge ID: {}, Set ID: {}, Total Records: {}", bridgeId, setId, conditionDTOs.size());	    
	    try {
	        for (BridgeConditionDTO dto : conditionDTOs) {
	            if (dto.getBridgeCondId() != null) {
	                Optional<BridgeCondition> existingCondition = bridgeConditionRepository.findById(dto.getBridgeCondId());
	                if (existingCondition.isPresent()) {
	                    BridgeCondition condition = existingCondition.get();
	                    condition.setAttributeType(dto.getAttributeType());
	                    condition.setAttributeValue(dto.getAttributeValue());
	                    condition.setLastUpdatedBy(Long.parseLong(userId));
	                    condition.setLastUpdatedAt(LocalDateTime.now().toString());
	                    bridgeConditionRepository.save(condition);
	                    log.info("Updated BridgeCondition: ID: {}, Member ID: {}, Bridge ID: {}, Set ID: {}", dto.getBridgeCondId(), dto.getMemberId(), bridgeId, setId);
	                } else {
	                    log.warn("BridgeCondition with ID {} not found. Skipping update.", dto.getBridgeCondId());
	                }
	            } else {
	                BridgeCondition newCondition = new BridgeCondition();
	                newCondition.setBridgeId(bridgeId);
	                newCondition.setSetId(setId);
	                newCondition.setMemberId(dto.getMemberId());
	                newCondition.setAttributeType(dto.getAttributeType());
	                newCondition.setAttributeValue(dto.getAttributeValue());
	                newCondition.setCreatedBy(userId);
	                newCondition.setCreatedAt(LocalDateTime.now().toString());
	                newCondition.setDeleted(false);
	                bridgeConditionRepository.save(newCondition);
	                log.info("Inserted new BridgeCondition: Member ID: {}, Bridge ID: {}, Set ID: {}", dto.getMemberId(), bridgeId, setId);
	            }
	        }
	    } catch (Exception e) {
	        log.error("Error while updating/inserting BridgeConditions. Bridge ID: {}, Set ID: {}", bridgeId, setId, e);
	        throw new RuntimeException("Failed to update/insert bridge conditions", e);
	    }	    
	    log.info("Successfully processed BridgeCondition records. Bridge ID: {}, Set ID: {}", bridgeId, setId);
	    return bridgeId;
	}
	

	@Override
	@Transactional(readOnly = true)
	public List<ConditionSurveyDateListResponseDTO> getSurveyDatesByBridgeId(Long bridgeId) {
		log.info("Fetching condition survey dates by getSurveyDatesByBridgeId method for bridgeId: {}", bridgeId);
		List<ConditionSurveyDateListResponseDTO> surveyDates = new ArrayList<>();
		try {
			List<Object[]> results = bridgeConditionRepository.findSurveyDatesByBridgeId(bridgeId);
			if (results == null || results.isEmpty()) {
				throw new CustomException("No survey dates found for bridge ID: " + bridgeId);
			}
			surveyDates = results.stream()
					.map(obj -> new ConditionSurveyDateListResponseDTO(((Number) obj[0]).longValue(), (String) obj[1]))
					.collect(Collectors.toList());

			log.debug("Survey dates retrieved successfully for the bridge ID: {}", bridgeId);
		} catch (CustomException e) {
			log.error("BridgeNotFoundException: {}", e.getMessage());
		} catch (Exception e) {
			log.error("Exception while retrieving survey dates for the bridge ID: {}", bridgeId, e);
		}
		return surveyDates;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getComponentsBySetId(@Min(1) Long setId) {
		try {
			log.info("Fetching distinct components for setId: {}", setId);

			List<String> memberIds = bridgeConditionRepository.findComponentsBySetIdExcludingCond(setId);

			if (memberIds.isEmpty()) {
				log.warn("No member_ids found for setId: {}", setId);
			} else {
				log.info("Successfully fetched {} components for setId: {}", memberIds.size(), setId);
			}

			return memberIds;
		} catch (Exception e) {
			log.error("Error fetching components for setId: {}. Error: {}", setId, e.getMessage());
			throw new CustomException("Failed to fetch components for the provided setId.");
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<BridgeConditionResponseDTO> getRecordsBySetIdAndMemberId(Long setId, String memberId) {
	    List<BridgeConditionResponseDTO> bridgeConditionDTOs = new ArrayList<>();
	    try {
	        log.info("Fetching records with setId: {}{}", setId, memberId != null ? " and memberId: " + memberId : "");
	        
	        List<BridgeCondition> bridgeConditions;
	        if (memberId != null && !memberId.isEmpty()) {
	            bridgeConditions = bridgeConditionRepository.findBySetIdAndMemberId(setId, memberId);
	        } else {
	            bridgeConditions = bridgeConditionRepository.findBySetId(setId);
	        }	        
	        log.info("Number of records fetched: {}", bridgeConditions.size());	        
	        bridgeConditionDTOs = bridgeConditions.stream().map(this::convertToDTO).collect(Collectors.toList());
	        log.info("Successfully converted BridgeCondition entities to BridgeConditionDTOs.");	        
	    } catch (Exception e) {
	        log.error("An error occurred while fetching records: ", e);
	    }
	    return bridgeConditionDTOs;
	}

	
	@Override
    @Transactional
    public int deleteBridgeConditions(Long setId, String memberId, Long userId) {
        if (setId == null || userId == null) {
            throw new IllegalArgumentException("setId and userId cannot be null.");
        }
        String lastUpdatedAt = LocalDateTime.now().toString();
        try {
            if (memberId != null && !memberId.trim().isEmpty()) {
                log.debug("Deleting bridge condition for setId: {} and memberId: {}", setId, memberId);
                return bridgeConditionRepository.deleteBySetIdAndMemberId(setId, memberId, userId, lastUpdatedAt);
            } else {
                log.debug("Deleting bridge condition for setId: {} only", setId);
                return bridgeConditionRepository.deleteBySetId(setId, userId, lastUpdatedAt);
            }
        } catch (Exception ex) {
            log.error("Error deleting bridge conditions for setId: {} and memberId: {}: {}", setId, memberId, ex.getMessage(), ex);
            throw ex;
        }
    }
	
	@Override
	public byte[] generateReportBridgeCondition(Long userId, Long bridgeId, Long setId) {
	    log.info("Inside generateReportBridgeInventory method with: userId = {}", userId);
	    return generateReportInternal(userId, bridgeId, setId, "bridge_condition.jrxml");
	}

	private byte[] generateReportInternal(Long userIdLong, Long bridgeId, Long setId, String reportName) {
	    try {
	        String macAddress = NetworkUtils.getMacAddress();
	        log.info("MAC Address: {}", macAddress);
	        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa", Locale.ENGLISH);
	        String currentDateTime = simpleDateFormat.format(new Date());
	        log.info("Date Time: {}", currentDateTime);
	        String userName = bridgeConditionViewRepository.getUserNameById(userIdLong);
	        if (userName == null) {
	            log.error("User not found for userId: {}", userIdLong);
	            throw new IllegalArgumentException("Invalid userId: " + userIdLong);
	        }
	        log.info("userName: {}", userName);
	        List<BridgeConditionView> data = bridgeConditionViewRepository.findByBridgeIdAndSetId(bridgeId, setId);
	        log.info("StartChainage: {}", data.stream().map(BridgeConditionView::getBridgeId).collect(Collectors.toList()));
	        if (data.isEmpty()) {
	            log.warn("No data found for bridgeId: {}", bridgeId);
	        }
	        Map<String, Object> parameters = new HashMap<>();
	        parameters.put("userId", userName);
	        parameters.put("macAddress", macAddress);
	        parameters.put("dateTime", currentDateTime);
	        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);
	        JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/static/report/" + reportName));
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
	        return JasperExportManager.exportReportToPdf(jasperPrint);
	    } catch (JRException e) {
	        log.error("Error generating report", e);
	        throw new RuntimeException("Error generating report", e);
	    } catch (IllegalArgumentException e) {
	        log.error("Invalid argument: {}", e.getMessage(), e);
	        throw e;
	    } catch (Exception e) {
	        log.error("Unexpected error generating report: {}", e.getMessage(), e);
	        throw new RuntimeException("An unexpected error occurred while generating the report. Please try again later.", e);
	    }
	}

	// Convert BridgeCondition entity to BridgeConditionDTO
	private BridgeConditionResponseDTO convertToDTO(BridgeCondition bridgeCondition) {
		BridgeConditionResponseDTO dto = new BridgeConditionResponseDTO();
		try {
			log.info("Converting BridgeCondition entity with ID: {}", bridgeCondition.getBridgeCondId());
			dto.setBridgeCondId(bridgeCondition.getBridgeCondId());
			dto.setMemberId(bridgeCondition.getMemberId());
			dto.setAttributeType(bridgeCondition.getAttributeType());
			dto.setAttributeValue(bridgeCondition.getAttributeValue());
			log.info("Successfully converted BridgeCondition entity to BridgeConditionResponseDTO.");
		} catch (Exception e) {
			log.error("An error occurred while converting BridgeCondition entity to DTO: ", e);
		}
		return dto;
	}
	
	@Override
	public boolean isDataAvailable(Long bridgeId, String memberId) {
		long count = bridgeConditionRepository.countByBridgeIdAndOptionalMemberId(bridgeId, memberId);
		log.info("Query Result: Found {} records for bridgeId: {} with memberId: {}", count, bridgeId, (memberId == null || memberId.isEmpty()) ? "ALL" : memberId);
		return count > 0;
	}
}
