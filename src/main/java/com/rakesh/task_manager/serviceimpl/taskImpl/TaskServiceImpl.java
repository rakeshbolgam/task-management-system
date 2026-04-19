package com.rakesh.task_manager.serviceimpl.taskImpl;

import com.rakesh.task_manager.dto.ListOfTasksDTO;
import com.rakesh.task_manager.dto.TaskDTO;
import com.rakesh.task_manager.dto.payload.TaskRequest;
import com.rakesh.task_manager.entity.Tasks;
import com.rakesh.task_manager.entity.Users;
import com.rakesh.task_manager.exceptions.APIException;
import com.rakesh.task_manager.repo.TasksRepo;
import com.rakesh.task_manager.repo.UsersRepo;
import com.rakesh.task_manager.service.security.utils.AuthUtil;
import com.rakesh.task_manager.service.task.TaskService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TasksRepo tasksRepo;
    private final UsersRepo usersRepo;
    private final AuthUtil authUtil;
    @Override
    public String crateTask(TaskRequest taskRequest) {
        boolean exists = tasksRepo.existsByTitleAndUserId(taskRequest.getTitle(), taskRequest.getUserId());
        if(exists){
            throw new APIException("The Task Already created for this user....");
        }
        Tasks task = toTaskEntity(taskRequest);
//        Users user = usersRepo.findById(taskRequest.getUserId()).orElseThrow(
//                () -> new APIException("User Not Found")
//        );
        tasksRepo.save(task);
        return "task is created for the user "+taskRequest.getUserId();
    }

    @Override
    public ListOfTasksDTO listOfTasks(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        Page<Tasks> pageOfTasks = tasksRepo.findAll(pageRequest);
        List<Tasks> content = pageOfTasks.getContent();
        if(content.isEmpty())
            throw new APIException("There is no tasks created");
        List<TaskDTO> dtoList = content.stream().map(
                task -> toTaskDTO(task)
        ).toList();
        ListOfTasksDTO listOfTasksDTO = new ListOfTasksDTO();
        listOfTasksDTO.setListOfTasks(dtoList);
        listOfTasksDTO.setPageNumber(pageOfTasks.getNumber());
        listOfTasksDTO.setPageSize(pageOfTasks.getSize());
        listOfTasksDTO.setTotalPages(pageOfTasks.getTotalPages());
        listOfTasksDTO.setLastPage(pageOfTasks.isLast());
        listOfTasksDTO.setTotalElements(pageOfTasks.getTotalElements());
        return listOfTasksDTO;
    }

    @Override
    public ListOfTasksDTO listOfTasksById(Long userId,Integer pageNumber,Integer pageSize, String sortBy,String sortDir) {
        Users loggedInUser = authUtil.loggedInUser();
        Long effectiveUserId;
        if(userId==null){
            effectiveUserId = loggedInUser.getId();
        }else{
            boolean isAdmin = loggedInUser.getRole().equals("ROLE_ADMIN");
            if(!isAdmin)
                throw new APIException("Access denied!!!");
            effectiveUserId = userId;
        }
        Sort sort = sortDir.equalsIgnoreCase("asc")?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Tasks> pageOfTasksByUserId = tasksRepo.findByUser_Id(effectiveUserId, pageable);
        List<Tasks> content = pageOfTasksByUserId.getContent();
        if(content.isEmpty())
            throw new APIException("No Tasks were created for this user");
        List<TaskDTO> dtoList = content.stream().map(
                task -> toTaskDTO(task)
        ).toList();
        ListOfTasksDTO tasksDTO =new ListOfTasksDTO();
        tasksDTO.setListOfTasks(dtoList);
        tasksDTO.setPageNumber(pageOfTasksByUserId.getNumber());
        tasksDTO.setPageSize(pageOfTasksByUserId.getSize());
        tasksDTO.setTotalPages(pageOfTasksByUserId.getTotalPages());
        tasksDTO.setTotalElements(pageOfTasksByUserId.getTotalElements());
        tasksDTO.setLastPage(pageOfTasksByUserId.isLast());
        return tasksDTO;
    }

    @Override
    public TaskDTO updateTasks(TaskRequest taskRequest) {
        Tasks tasks = tasksRepo.findByTitleAndUserId(taskRequest.getTitle(), taskRequest.getUserId()).orElseThrow(
                () -> new APIException("not tasks created for the user...")
        );
        tasks.setTitle(taskRequest.getTitle());
        tasks.setStatus(taskRequest.getStatus());
        tasks.setDescription(taskRequest.getDescription());
        tasks.setPriority(taskRequest.getPriority());
        Users user = usersRepo.findById(taskRequest.getUserId()).orElseThrow(
                () -> new APIException("no user found with " + taskRequest.getUserId())
        );
        tasks.setUser(user);
        tasks.setDueDate(taskRequest.getDueDate());
        Tasks save = tasksRepo.save(tasks);

        return toTaskDTO(save);
    }

    @Override
    public String deleteTask(Long taskId) {
        if (!tasksRepo.existsById(taskId)) {
            throw new APIException("Task not found with id: " + taskId);
        }
        tasksRepo.deleteById(taskId);
        return "task is removed successfully!!!";
    }

    public Tasks toTaskEntity(TaskRequest request){
        Tasks entity=new Tasks();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus());
        entity.setPriority(request.getPriority());
        entity.setDueDate(request.getDueDate());
        Users user = usersRepo.findById(request.getUserId()).orElseThrow(
                () -> new APIException("User Not Found...")
        );
        entity.setUser(user);
        return entity;
    }

    private TaskDTO toTaskDTO(Tasks task) {
        TaskDTO dto = new TaskDTO();
        dto.setTaskId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
//        Users user = usersRepo.findById(task.getUser().getId()).orElseThrow(() -> new APIException("User Not found"));
//        dto.setUser(task.getUser());
        Users user = task.getUser();
        dto.setUserId(user.getId());
        dto.setUserName(user.getUsername());
        dto.setCreateDate(task.getCreatedAt());
        dto.setUpdateDate(task.getUpdatedAt());
        dto.setDueDate(task.getDueDate());
        return dto;
    }
}
