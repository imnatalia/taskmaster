package br.ifsp.taskmaster.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.ifsp.taskmaster.exception.ResourceNotFoundException;
import br.ifsp.taskmaster.model.Task;
import br.ifsp.taskmaster.repository.TaskRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public Page<Task> getAllTasks(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    public Page<Task> getTasksByCategory(String category, Pageable pageable) {
        return taskRepository.findByCategory(category, pageable);
    }
    
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Tarefa não encontrada com o ID: " + id));
    }

    public Boolean existsById(Long id) {
        return taskRepository.existsById(id);
    }

    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }
}
