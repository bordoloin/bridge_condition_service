/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.dtos;

import java.io.Serializable;

/**
 * @author Nobol
 *
 */

public class BridgeConditionResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long bridgeCondId;
	private String memberId;
	private String attributeType;
	private String attributeValue;
	
	public Long getBridgeCondId() {
		return bridgeCondId;
	}
	public void setBridgeCondId(Long bridgeCondId) {
		this.bridgeCondId = bridgeCondId;
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
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public BridgeConditionResponseDTO() {
		super();
	}
	public BridgeConditionResponseDTO(Long bridgeCondId, String memberId, String attributeType, String attributeValue) {
		super();
		this.bridgeCondId = bridgeCondId;
		this.memberId = memberId;
		this.attributeType = attributeType;
		this.attributeValue = attributeValue;
	}
}
