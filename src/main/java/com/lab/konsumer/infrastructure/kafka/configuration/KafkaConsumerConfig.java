package com.lab.konsumer.infrastructure.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import com.lab.konsumer.domain.model.Question;
import com.lab.konsumer.infrastructure.kafka.messageConverters.QuestionMessageDeserializer;


/**
 * Clase de configuración para los listeners de Kafka (Consumidores).
 * Gestiona la creación del factory y el contenedor para asegurar que los mensajes 
 * se deserialicen correctamente en el dominio esperado.
 */
@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    /**
     * Bean que configura el factory de los mensajes consumidos (el consumer factory).
     * Se inicializa manualmente con propiedades por defecto o se deberían usar 
     * las clases Spring Kafka Properties para una configuración real.
     */
    @Bean
    public ConsumerFactory<String, Question> consumerFactory() {

        // En un proyecto real, estas propiedades vendrían de application.yml/properties
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"); // Ejemplo
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "konsumer-group"); // Ejemplo
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, QuestionMessageDeserializer.class);
        // props.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        // props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, KafkaAvroDese);
        // return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JacksonJsonDeserializer<>(Question.class));
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Question> kafkaListenerContainerFactory() {
        
        ConcurrentKafkaListenerContainerFactory<String, Question> factory = 
                new ConcurrentKafkaListenerContainerFactory<>();
        DefaultErrorHandler errorHandler = new DefaultErrorHandler(
            (record, exception) -> {
                System.err.println("fail at message by: " + exception.getMessage());
            },
            new FixedBackOff(1000L, 3L)
        );
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler);
        return factory;
    }
}