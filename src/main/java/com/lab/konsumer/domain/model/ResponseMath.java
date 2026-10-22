package com.lab.konsumer.domain.model;

/**
 * Representa la respuesta generada por un modelo de lenguaje o cálculo.
 * Usando Java Record para inmutabilidad y concisión.
 */
public record ResponseMath(
        Long awnserId,
        String response,
        String modelUsed,
        Integer costTokens
) {
}