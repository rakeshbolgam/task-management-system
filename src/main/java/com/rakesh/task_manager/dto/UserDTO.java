package com.rakesh.task_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserDTO {
    private Long usreId;
    private String userName;
    private String email;
    private String role;
    private String status;
}
