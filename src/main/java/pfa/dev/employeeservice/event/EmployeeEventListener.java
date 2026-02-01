package pfa.dev.employeeservice.event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
import pfa.dev.employeeservice.kafka.EmployeeProducer;

@Component
@RequiredArgsConstructor
public class EmployeeEventListener {

    private final EmployeeProducer employeeProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onEmployeeCreated(EmployeeCreatedEvent event) {
        employeeProducer.sendEmployeeCreated(event);
    }
}
