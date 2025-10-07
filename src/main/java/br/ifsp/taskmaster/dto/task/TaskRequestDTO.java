package br.ifsp.taskmaster.dto.task;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskRequestDTO {
    @NotBlank(message = "O título não pode ser vazio")
    String title;
    
    @NotBlank(message = "A descrição não pode ser vazia")
    String description;

    @NotBlank(message = "A categoria não pode ser vazia")
    String category;

    @FutureOrPresent(message = "A data limite deve ser hoje ou no futuro")
    LocalDate limitDate;
}
