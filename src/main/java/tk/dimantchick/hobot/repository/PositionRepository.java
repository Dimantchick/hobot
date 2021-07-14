package tk.dimantchick.hobot.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.dimantchick.hobot.domain.position.HobotPosition;
import tk.dimantchick.hobot.domain.position.PositionStatus;

import java.util.Set;

@Repository
public interface PositionRepository extends PagingAndSortingRepository<HobotPosition, Long> {
    //@Transactional(propagation=Propagation.REQUIRED)
    Set<HobotPosition> findByStatusNot(PositionStatus status);
    //@Transactional(propagation=Propagation.REQUIRED)
    Set<HobotPosition> findAll(Sort sort);

    //@Transactional(propagation=Propagation.REQUIRED)
    Set<HobotPosition> findAll();



}
