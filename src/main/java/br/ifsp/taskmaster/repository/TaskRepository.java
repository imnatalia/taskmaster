package br.ifsp.taskmaster.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.ifsp.taskmaster.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByCategory(String category, Pageable pageable);
}
