/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.services;

import java.util.List;

import jakarta.validation.constraints.Min;
import lea.rams.bis.bridge.condition.service.ws.dtos.BridgeConditionDTO;
import lea.rams.bis.bridge.condition.service.ws.dtos.BridgeConditionResponseDTO;
import lea.rams.bis.bridge.condition.service.ws.dtos.ConditionSurveyDateListResponseDTO;

/**
 * @author Nobol
 *
 */
public interface BridgeConditionService {

	Long saveBridgeConditions(Long bridgeId, List<BridgeConditionDTO> conditionDTOs);

	List<ConditionSurveyDateListResponseDTO> getSurveyDatesByBridgeId(Long bridgeId);

	List<String> getComponentsBySetId(@Min(1) Long setId);

	List<BridgeConditionResponseDTO> getRecordsBySetIdAndMemberId(Long setId, String memberId);

	int deleteBridgeConditions(Long setId, String memberId, Long userId);

	boolean isDataAvailable(Long bridgeId, String memberId);

	Long updateBridgeConditions(Long bridgeId, Long setId, List<BridgeConditionDTO> conditionDTOs, String userId);

	byte[] generateReportBridgeCondition(Long userId, Long bridgeId, Long setId);

}
