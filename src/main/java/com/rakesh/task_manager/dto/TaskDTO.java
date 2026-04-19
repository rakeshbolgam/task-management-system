package com.rakesh.task_manager.dto;

import com.rakesh.task_manager.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskDTO {
    private Long taskId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private LocalDateTime dueDate;
//    private Users user;
    private Long userId;
    private String userName;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
}
