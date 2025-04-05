/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.dtos;

import java.io.Serializable;

/**
 * @author Nobol
 *
 */

public class BridgeConditionDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long bridgeCondId;
	private Long bridgeId;
	private Long setId;
	private String memberId;
	private String attributeType;
	private String attributeValue;
	
	public Long getBridgeCondId() {
		return bridgeCondId;
	}
	public void setBridgeCondId(Long bridgeCondId) {
		this.bridgeCondId = bridgeCondId;
	}
	public Long getBridgeId() {
		return bridgeId;
	}
	public void setBridgeId(Long bridgeId) {
		this.bridgeId = bridgeId;
	}
	public Long getSetId() {
		return setId;
	}
	public void setSetId(Long setId) {
		this.setId = setId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getAttributeType() {
		return attributeType;
	}
	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
}
