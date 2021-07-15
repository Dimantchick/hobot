package tk.dimantchick.hobot.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tk.dimantchick.hobot.domain.position.HobotPosition;
import tk.dimantchick.hobot.domain.position.PositionStatus;

import java.util.Set;

/**
 * Репозиторий позиций.
 */
@Repository
public interface PositionRepository extends PagingAndSortingRepository<HobotPosition, Long> {

    Set<HobotPosition> findByStatusNot(PositionStatus status);

    Set<HobotPosition> findAll(Sort sort);

    Set<HobotPosition> findAll();



}
