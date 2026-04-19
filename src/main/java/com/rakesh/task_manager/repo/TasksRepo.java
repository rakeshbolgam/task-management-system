package com.rakesh.task_manager.repo;

import com.rakesh.task_manager.entity.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasksRepo extends JpaRepository<Tasks, Long> {
    boolean existsByTitleAndUserId(String title, Long userId);

    @EntityGraph(attributePaths = {"user"})
    Page<Tasks> findByUser_Id(Long userId, Pageable pageRequest);

    @EntityGraph(attributePaths = {"user"})
    Optional<Tasks> findByTitleAndUserId(String title, Long userId);
}
