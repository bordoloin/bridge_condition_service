/**
 * 
 */
package lea.rams.bis.bridge.condition.service.ws.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lea.rams.bis.bridge.condition.service.ws.entities.BridgeConditionView;

/**
 * @author Nobol
 *
 */
public interface BridgeConditionViewRepository extends JpaRepository<BridgeConditionView, Long> {
	
	@Query(value = "SELECT get_user_name_by_id(:userId)", nativeQuery = true)
	String getUserNameById(@Param("userId") Long userId);

	@Query(value = "SELECT * FROM rep_bis_bridge_condition_view WHERE bridge_id = :bridgeId AND set_id = :setId", nativeQuery = true)
	List<BridgeConditionView> findByBridgeIdAndSetId(Long bridgeId, Long setId);

}
