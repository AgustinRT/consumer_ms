package com.lab.konsumer.domain.model;

import java.util.List;

/**
 * Representa una pregunta o prompt de usuario en el sistema.
 * Usando Java Record para inmutabilidad y concisión.
 */
public record Question(
        Long id,
        String prompt,
        List<String> tags) {
}