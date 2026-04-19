package com.rakesh.task_manager.service.security.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserInfoResponse {

    private Long id;
    private String username;
    private String token;
    private List<String> roles;

    public UserInfoResponse(Long id, String username, List<String> roles){
        this.id=id;
        this.username=username;
        this.roles=roles;
    }
}
