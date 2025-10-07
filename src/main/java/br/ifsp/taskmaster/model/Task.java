package br.ifsp.taskmaster.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @NotBlank(message = "O título não pode ser vazio")
    String title;
    
    @NotBlank(message = "A descrição não pode ser vazia")
    String description;

    @NotBlank(message = "A categoria não pode ser vazia")
    String category;

    @FutureOrPresent(message = "A data limite deve ser hoje ou no futuro")
    LocalDate limitDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    User user;
}
