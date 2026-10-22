package com.lab.konsumer.infrastructure.kafka.listener;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import com.lab.konsumer.domain.model.Question;

/**
 * Adapter Inbound encargado de consumir mensajes del tópico "question".
 * Implementa el Listener Kafka para pruebas de concepto (PoC).
 */
@Component
public class QuestionListener {

    /**
     * Método que se activa cuando recibe un mensaje en el tópico "question".
     * 
     * @param message El payload recibido del mensaje Kafka.
     */
    @RetryableTopic(attempts="1", dltStrategy=DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(topics = "topic-question-events", groupId = "konsumer-group")
    public void handleQuestionMessage(Question question) {
        // En esta etapa de PoC, simplemente imprimimos el mensaje.
        System.out.println("--- [Adapter Inbound] Consumido mensaje del tópico 'topic-question-events' ---");
        System.out.println("Mensaje recibido: " + question.prompt());
        System.out.println("----------------------------------------------------------");
    }

    @DltHandler 
    public void handleDltPayment(Question question, @Header (KafkaHeaders.RECEIVED_TOPIC) String topic){
        System.out.println("Event on dlt topic= " + topic + ", payload= " + question.prompt());
    }

    @DltHandler 
    public void handleDltPayment(String question, @Header (KafkaHeaders.RECEIVED_TOPIC) String topic){
        System.out.println("Event on dlt topic= " + topic + ", payload= " + question);
    }
}