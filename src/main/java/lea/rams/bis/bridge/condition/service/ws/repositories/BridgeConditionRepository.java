/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import lea.rams.bis.bridge.condition.service.ws.entities.BridgeCondition;

/**
 * @author Nobol
 *
 */
public interface BridgeConditionRepository extends JpaRepository<BridgeCondition, Long> {

	@Query(value = "SELECT BC_SET_ID AS SET_ID, TO_CHAR(TO_DATE(BC_ATTRIB_STATUS, 'MM/DD/YYYY'), 'DD-Mon-YYYY') AS SURVEY_DATE FROM BRIDGE_CONDITION "
			+ "WHERE BC_BRIDGE_ID = ? AND BC_ATTRIBUTE_TYPE = 'FORSURVEYDATE' AND IS_DELETED IS FALSE ORDER BY BC_SET_ID DESC", nativeQuery = true)
	List<Object[]> findSurveyDatesByBridgeId(@Param("bridgeId") Long bridgeId);
	
	
	@Query(value = """
			SELECT bc_component FROM (SELECT DISTINCT b.bc_component AS bc_component,
			        CASE
			            WHEN b.bc_component = 'A1' THEN 1
			            WHEN b.bc_component LIKE 'P%' AND LENGTH(b.bc_component) BETWEEN 2 AND 3 THEN 2
			            WHEN b.bc_component = 'A2' THEN 3
			            WHEN b.bc_component LIKE 'S%' AND LENGTH(b.bc_component) BETWEEN 2 AND 3 THEN 4
			            WHEN b.bc_component LIKE '%P%' AND LENGTH(b.bc_component) BETWEEN 3 AND 4 THEN 5
			            WHEN b.bc_component LIKE 'W%' THEN 6
			            ELSE 7  -- Any other unexpected values
			        END AS sort_order
			    FROM bridge_condition b WHERE b.is_deleted = false  AND b.bc_set_id = :setId AND b.bc_component <> 'COND'
			) subquery ORDER BY subquery.sort_order, subquery.bc_component
				""", nativeQuery = true)
	List<String> findComponentsBySetIdExcludingCond(@Param("setId") Long setId);	

	@Query(value = "SELECT * FROM bridge_condition WHERE bc_set_id = :setId AND bc_component = :memberId AND is_deleted = false", nativeQuery = true)
	List<BridgeCondition> findBySetIdAndMemberId(@Param("setId") Long setId, @Param("memberId") String memberId);
	
	@Modifying
    @Transactional
    @Query("UPDATE BridgeCondition bc SET bc.isDeleted = true, bc.lastUpdatedBy = :userId, bc.lastUpdatedAt = :lastUpdatedAt WHERE bc.setId = :setId AND bc.isDeleted = false")
    int deleteBySetId(@Param("setId") Long setId, @Param("userId") Long userId, @Param("lastUpdatedAt") String lastUpdatedAt);

    @Modifying
    @Transactional
    @Query("UPDATE BridgeCondition bc SET bc.isDeleted = true, bc.lastUpdatedBy = :userId, bc.lastUpdatedAt = :lastUpdatedAt WHERE bc.setId = :setId AND bc.memberId = :memberId AND bc.isDeleted = false")
    int deleteBySetIdAndMemberId(@Param("setId") Long setId, @Param("memberId") String memberId, @Param("userId") Long userId, @Param("lastUpdatedAt") String lastUpdatedAt);
    
	@Query("SELECT COUNT(b) FROM BridgeCondition b WHERE b.bridgeId = :bridgeId AND (:memberId IS NULL OR :memberId = '' OR LOWER(b.memberId) = LOWER(:memberId)) AND b.isDeleted = false")
	long countByBridgeIdAndOptionalMemberId(@Param("bridgeId") Long bridgeId, @Param("memberId") String memberId);
	
	@Query(value = "SELECT * FROM bridge_condition b WHERE b.bc_set_id = :setId AND b.is_deleted = false \n" +
		       "ORDER BY \n" +
		       "CASE \n" +
		       "    WHEN b.bc_component = 'A1' THEN 1 \n" +
		       "    WHEN b.bc_component LIKE 'P%' AND LENGTH(b.bc_component) BETWEEN 2 AND 3 THEN 2 \n" +
		       "    WHEN b.bc_component = 'A2' THEN 3 \n" +
		       "    WHEN b.bc_component LIKE 'S%' AND LENGTH(b.bc_component) BETWEEN 2 AND 3 THEN 4 \n" +
		       "    WHEN b.bc_component LIKE '%P%' AND LENGTH(b.bc_component) BETWEEN 3 AND 4 THEN 5 \n" +
		       "    WHEN b.bc_component LIKE 'W%' THEN 6 \n" +
		       "    ELSE 7  \n" +
		       "END", nativeQuery = true)
		List<BridgeCondition> findBySetId(@Param("setId") Long setId);

}
