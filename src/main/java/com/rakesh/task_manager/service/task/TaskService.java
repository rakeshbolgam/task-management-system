package com.rakesh.task_manager.service.task;

import com.rakesh.task_manager.dto.ListOfTasksDTO;
import com.rakesh.task_manager.dto.TaskDTO;
import com.rakesh.task_manager.dto.payload.TaskRequest;

public interface TaskService {
    String crateTask(TaskRequest taskRequest);

    ListOfTasksDTO listOfTasks(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    ListOfTasksDTO listOfTasksById(Long userId,Integer pageNumber,Integer pageSize, String sortBy,String sortDir);

    TaskDTO updateTasks(TaskRequest taskRequest);

    String deleteTask(Long taskId);
}
