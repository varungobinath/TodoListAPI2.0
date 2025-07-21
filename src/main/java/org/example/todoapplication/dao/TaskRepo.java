package org.example.todoapplication.dao;

import jakarta.transaction.Transactional;
import org.example.todoapplication.model.Task;
import org.example.todoapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepo extends JpaRepository<Task,Long> {
    List<Task> findByUser(User user);
    List<Task> findByUserAndCompletedIs(User user, boolean completed);

    @Transactional
    void deleteByUserId(Long id);
}
