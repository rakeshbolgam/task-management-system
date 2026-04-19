package com.rakesh.task_manager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tasks",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"title","userId"}
        )
)
@Data
public class Tasks {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    @Column(columnDefinition = "varchar(255) default 'pending'")
//    @ColumnDefault("pending")
    private String status;
    @Column(columnDefinition = "varchar(255) default 'medium'")
    private String priority;
    private LocalDateTime dueDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private Users user;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
