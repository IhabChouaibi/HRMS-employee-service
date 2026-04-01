package pfa.dev.employeeservice.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import pfa.dev.employeeservice.config.KafkaConfig;
import pfa.dev.employeeservice.event.EmployeeCreatedEvent;
import pfa.dev.employeeservice.event.EmployeeDeletedEvent;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEmployeeCreated(EmployeeCreatedEvent event) {
        send(KafkaConfig.EMPLOYEE_CREATED, event.userId(), event, "EmployeeCreatedEvent");
    }

    public void sendEmployeeDeleted(EmployeeDeletedEvent event) {
        send(KafkaConfig.EMPLOYEE_DELETED, event.userId(), event, "EmployeeDeletedEvent");
    }

    private void send(String topic, String key, Object event, String eventName) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("{} sent to topic={} offset={}", eventName, topic, result.getRecordMetadata().offset());
                return;
            }

            log.error("Failed to send {} to topic={}", eventName, topic, ex);
        });
    }
}
