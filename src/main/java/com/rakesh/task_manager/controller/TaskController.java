package com.rakesh.task_manager.controller;

import com.rakesh.task_manager.constants.AppConstant;
import com.rakesh.task_manager.dto.ListOfTasksDTO;
import com.rakesh.task_manager.dto.TaskDTO;
import com.rakesh.task_manager.dto.payload.MessageResponse;
import com.rakesh.task_manager.dto.payload.TaskRequest;
import com.rakesh.task_manager.service.security.jwt.JwtUtils;
import com.rakesh.task_manager.service.security.utils.AuthUtil;
import com.rakesh.task_manager.service.task.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final AuthUtil authUtil;
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody @Valid TaskRequest taskRequest){
        String body = taskService.crateTask(taskRequest);
        MessageResponse response = new MessageResponse();
        response.setMessage(body);
        response.setSuccess(true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/list")
    public ResponseEntity<ListOfTasksDTO> listOfTasks(@RequestParam(name = "pageNumber" , defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
                                                      @RequestParam(name = "pageSize" , defaultValue = AppConstant.PAGE_SIZE) Integer pageSize,
                                                      @RequestParam(name = "sortBy" , defaultValue = AppConstant.SORT_BY_TITLE) String sortBy,
                                                      @RequestParam(name = "sortDir", defaultValue = AppConstant.SORT_DIR) String sortDir){
        ListOfTasksDTO listOfTasksDTO = taskService.listOfTasks(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(listOfTasksDTO);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @GetMapping("/listById")
    public ResponseEntity<ListOfTasksDTO> listOfTaksById(@RequestParam(required = false) Long userId,
                                            @RequestParam(name = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER) Integer pageNumber,
                                            @RequestParam(name = "pageSize", defaultValue = AppConstant.PAGE_SIZE) Integer pageSize,
                                            @RequestParam(name = "sortBy", defaultValue = AppConstant.SORT_BY_TITLE) String sortBy,
                                            @RequestParam(name = "sortDir", defaultValue = AppConstant.SORT_DIR) String sortDir){
        ListOfTasksDTO listOfTasksDTO = taskService.listOfTasksById(userId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(listOfTasksDTO);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<TaskDTO> updateTasks(@RequestBody @Valid TaskRequest taskRequest){
        TaskDTO taskDTO = taskService.updateTasks(taskRequest);
        return ResponseEntity.ok(taskDTO);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<MessageResponse> deleteTaks(@RequestParam Long taskId){

        String s = taskService.deleteTask(taskId);
        MessageResponse response = new MessageResponse();
        response.setMessage(s);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }
}
