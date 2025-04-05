/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.entities;

import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * @author Nobol
 *
 */

@Entity
@SQLDelete(sql = "update bridge_condition set is_deleted = true where bd_id=?")
@Table(name = "bridge_condition")
public class BridgeCondition {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "bd_id")
	private Long bridgeCondId;

	@NotNull(message = "Bridge ID cannot be null")
    @Column(name = "bc_bridge_id", nullable = false)
	private Long bridgeId;
	
	@NotNull(message = "Condition Set ID cannot be null")
    @Column(name = "bc_set_id", nullable = false)
	private Long setId;
	
	@Size(max = 50)
	@Column(name = "bc_component")
	private String memberId;
	
	@Size(max = 100)
	@Column(name = "bc_attribute_type")
	private String attributeType;
	
	@Column(name = "bc_attrib_status", length=10485760)
	private String attributeValue;
	
	@Column(name = "bc_inserted_by")
	private String createdBy;

	@Column(name = "bc_updated_by")
	private Long lastUpdatedBy;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted = false;

	@Column(name = "bc_inserted_date")
	private String createdAt;

	@Column(name = "bc_updated_date")
	private String lastUpdatedAt;

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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(Long lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(String lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}
}
