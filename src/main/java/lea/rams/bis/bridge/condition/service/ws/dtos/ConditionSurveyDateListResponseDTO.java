/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.dtos;

import java.io.Serializable;

/**
 * @author Nobol
 *
 */
public class ConditionSurveyDateListResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long setId;
    private String surveyDate;
    
	public Long getSetId() {
		return setId;
	}
	public void setSetId(Long setId) {
		this.setId = setId;
	}
	public String getSurveyDate() {
		return surveyDate;
	}
	public void setSurveyDate(String surveyDate) {
		this.surveyDate = surveyDate;
	}
	public ConditionSurveyDateListResponseDTO(Long setId, String surveyDate) {
		super();
		this.setId = setId;
		this.surveyDate = surveyDate;
	}
}
