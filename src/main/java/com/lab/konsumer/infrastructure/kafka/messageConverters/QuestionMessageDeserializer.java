package com.lab.konsumer.infrastructure.kafka.messageConverters;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.kafka.common.serialization.Deserializer;

import com.lab.konsumer.domain.model.Question;

import tools.jackson.databind.ObjectMapper;

public class QuestionMessageDeserializer implements Deserializer<Question> {

    @Override
    public Question deserialize(String topic, byte[] data) {

        if(data == null) return null;

        ObjectMapper mapper = new ObjectMapper();
        try {
            Question message = mapper.readValue(new String(data, StandardCharsets.UTF_8), Question.class);
            return message;
        } catch (Exception e) {
            
            System.err.println("fail by: " + e.getMessage());
            String prompt = new String(data);
            return new Question(System.currentTimeMillis(), prompt, List.of(""));
        }
        // return null;
    }

}
