package pfa.dev.employeeservice.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import pfa.dev.employeeservice.config.KafkaConfig;
import pfa.dev.employeeservice.dto.EmployeeDto;
import pfa.dev.employeeservice.event.EmployeeCreatedEvent;

import java.util.concurrent.CompletableFuture;

@Service
public class EmployeeProducer {

    private final KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate;

    public EmployeeProducer(KafkaTemplate<String, EmployeeCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEmployeeCreated(EmployeeCreatedEvent event) {

        CompletableFuture<SendResult<String, EmployeeCreatedEvent>> future =
                kafkaTemplate.send(KafkaConfig.EMPLOYEE_CREATED, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println(
                        "EmployeeCreatedEvent sent with offset = "
                                + result.getRecordMetadata().offset()
                );
            } else {
                System.err.println(
                        "Failed to send EmployeeCreatedEvent : "
                                + ex.getMessage()
                );
            }
        });
    }
}
