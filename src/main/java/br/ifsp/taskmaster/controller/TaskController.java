package br.ifsp.taskmaster.controller;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.asm.Advice.Return;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.ifsp.taskmaster.dto.task.TaskRequestDTO;
import br.ifsp.taskmaster.dto.task.TaskResponseDTO;
import br.ifsp.taskmaster.exception.ResourceNotFoundException;
import br.ifsp.taskmaster.model.Task;
import br.ifsp.taskmaster.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Tasks", description = "API para gerenciamento de tarefas")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ModelMapper modelMapper;


    /* Requisições POST */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create Task", description = "Cria uma nova tarefa.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TaskResponseDTO> createTask(@Valid @RequestBody TaskRequestDTO taskRequestDTO) {
        Task task = modelMapper.map(taskRequestDTO, Task.class);
        Task savedTask = taskService.saveTask(task);
        
        TaskResponseDTO responseDTO = modelMapper.map(savedTask, TaskResponseDTO.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    /* Requisições PUT */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update Task", description = "Atualiza uma tarefa existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefa atualizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO taskRequestDTO) {
       Task existingTask = taskService.getTaskById(id);
       modelMapper.map(taskRequestDTO, existingTask);
       Task updatedTask = taskService.saveTask(existingTask);
       TaskResponseDTO responseDTO = modelMapper.map(updatedTask, TaskResponseDTO.class);

        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    /* Requisições DELETE */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Task", description = "Deleta uma tarefa existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tarefa deletada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (!taskService.existsById(id)) {
            throw new ResourceNotFoundException("Tarefa não encontrada com o ID: " + id);
        }
        taskService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /* Requisições GET */
    @Operation(summary = "Get All Tasks", description = "Retorna uma página com todas as tarefas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefas encontradas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "404", description = "Nenhuma tarefa encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<Page<TaskResponseDTO>> getAllTasks(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "5") int size,
                                                            @RequestParam(defaultValue = "title") String sort,
                                                            @RequestParam(defaultValue = "asc") String direction) {
        
        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        Page<Task> taskPage = taskService.getAllTasks(pageable);

        Page<TaskResponseDTO> taskResponsePage = taskPage.map(task -> modelMapper.map(task, TaskResponseDTO.class));
        return ResponseEntity.status(HttpStatus.OK).body(taskResponsePage);
    }

    @Operation(summary = "Get Tasks by Category", description = "Retorna uma página de tarefas filtradas por categoria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tarefas encontradas com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<Page<TaskResponseDTO>> getTasksByCategory(@PathVariable String category,
                                                                    Pageable pageable) {
        Page<Task> taskPage = taskService.getTasksByCategory(category, pageable);
        Page<TaskResponseDTO> taskResponsePage = taskPage.map(task -> modelMapper.map(task, TaskResponseDTO.class));
        
        return ResponseEntity.status(HttpStatus.OK).body(taskResponsePage);
    }
}