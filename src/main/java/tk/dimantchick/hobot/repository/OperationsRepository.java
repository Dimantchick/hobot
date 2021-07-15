package tk.dimantchick.hobot.repository;


import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import tk.dimantchick.hobot.domain.instrument.Instrument;
import tk.dimantchick.hobot.domain.operations.HobotOperation;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;


/**
 * Репозиторий операций.
 */
@Repository
public interface OperationsRepository extends PagingAndSortingRepository<HobotOperation, Long> {

    List<HobotOperation> findByPosition_Id(Long id);

    Optional<HobotOperation> findByIdAndInstrument(String id, Instrument instrument);

    Optional<HobotOperation> findById(String id);

    List<HobotOperation> findAll(Sort sort);

    void deleteByOperationDateBeforeAndPositionIsNull(OffsetDateTime date);
}
