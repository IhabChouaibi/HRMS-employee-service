package pfa.dev.employeeservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String EMPLOYEE_CREATED = "employee.created";
    public static final String EMPLOYEE_DELETED = "employee.deleted";

    @Bean
    public NewTopic employeeCreatedTopic() {
        return TopicBuilder.name(EMPLOYEE_CREATED)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic employeeDeletedTopic() {
        return TopicBuilder.name(EMPLOYEE_DELETED)
                .partitions(1)
                .replicas(1)
                .build();
    }

}
