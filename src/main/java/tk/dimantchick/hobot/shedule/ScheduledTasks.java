package tk.dimantchick.hobot.shedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.dimantchick.hobot.api.Api;
import tk.dimantchick.hobot.service.GrabbingService;
import tk.dimantchick.hobot.service.OperationsService;
import tk.dimantchick.hobot.service.PositionsService;

@Service
public class ScheduledTasks {

    private Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private final GrabbingService grabbingService;
    private final OperationsService operationsService;
    private final PositionsService positionsService;

    public ScheduledTasks(GrabbingService grabbingService, OperationsService operationsService, PositionsService positionsService) {
        this.grabbingService = grabbingService;
        this.operationsService = operationsService;
        this.positionsService = positionsService;
    }

    @Scheduled(fixedDelay = 100)
    public void grab5minCandles() {
        grabbingService.update5minCandles();
    }

    @Scheduled(cron = "30 0 4-23 * * *")
    public void grabHourCandles() {
        grabbingService.updateHourCandles();
    }

    @Scheduled(cron = "0 6,11,16,21,26,31,36,41,46,51,56 4-23 * * *")
    public void deleteOldCandles() {
        grabbingService.cleanOldCandles();
    }

    @Scheduled(fixedDelay = 10000)
    public void grabOperations() {
        operationsService.updateOperations();
    }

    @Scheduled(fixedDelay = 5000)
    public void workOnPositions() {
        positionsService.workOnPositions();
    }
}
