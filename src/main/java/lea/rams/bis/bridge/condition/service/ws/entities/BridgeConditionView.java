/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.entities;

import org.springframework.data.annotation.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Nobol
 *
 */

@Entity
@Immutable
@Table(name = "rep_bis_bridge_condition_view")
public class BridgeConditionView {
	
	@Id
	@Column(name = "id")
    private Long id;
	
	@Column(name = "bridge_id")
    private Long bridgeId;
	
	@Column(name = "set_id")
	private Long setId;
	
	@Column(name = "jrdcn_name")
    private String jrdcnName;

    @Column(name = "road_code")
    private Long roadCode;

    @Column(name = "rsi_id")
    private Long rsiId;
    
    @Column(name = "road_name")
    private String roadName;
    
    @Column(name = "lkp_road_category")
    private String roadCategory;
    
    @Column(name = "road_number")
    private String roadNumber;
    
    @Column(name = "road_direction")
    private String direction;
    
    @Column(name = "location")
    private Double location;
    
    @Column(name = "survey_date")
    private String surveyDate;
    
    @Column(name = "road_length")
    private Double roadLength;
    
    @Column(name = "member_id")
    private String memberId;
    
    @Column(name = "comp_attr")
    private String attributeType;
    
    @Column(name = "severity")
    private String severity;
    
    @Column(name = "extent")
    private String extent;
    
    @Column(name = "sub_comp_slno")
    private Double subComSlNo;
    
    @Column(name = "sl_no")
    private Double slNo;
    
    @Column(name = "remark")
    private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public String getJrdcnName() {
		return jrdcnName;
	}

	public void setJrdcnName(String jrdcnName) {
		this.jrdcnName = jrdcnName;
	}

	public Long getRoadCode() {
		return roadCode;
	}

	public void setRoadCode(Long roadCode) {
		this.roadCode = roadCode;
	}

	public Long getRsiId() {
		return rsiId;
	}

	public void setRsiId(Long rsiId) {
		this.rsiId = rsiId;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getRoadCategory() {
		return roadCategory;
	}

	public void setRoadCategory(String roadCategory) {
		this.roadCategory = roadCategory;
	}

	public String getRoadNumber() {
		return roadNumber;
	}

	public void setRoadNumber(String roadNumber) {
		this.roadNumber = roadNumber;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public Double getLocation() {
		return location;
	}

	public void setLocation(Double location) {
		this.location = location;
	}

	public String getSurveyDate() {
		return surveyDate;
	}

	public void setSurveyDate(String surveyDate) {
		this.surveyDate = surveyDate;
	}

	public Double getRoadLength() {
		return roadLength;
	}

	public void setRoadLength(Double roadLength) {
		this.roadLength = roadLength;
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

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getExtent() {
		return extent;
	}

	public void setExtent(String extent) {
		this.extent = extent;
	}

	public Double getSubComSlNo() {
		return subComSlNo;
	}

	public void setSubComSlNo(Double subComSlNo) {
		this.subComSlNo = subComSlNo;
	}

	public Double getSlNo() {
		return slNo;
	}

	public void setSlNo(Double slNo) {
		this.slNo = slNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
