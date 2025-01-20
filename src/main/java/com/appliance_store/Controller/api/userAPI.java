package com.appliance_store.Controller.api;

import com.appliance_store.Entity.Users;
import com.appliance_store.Service.user_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class userAPI
{
    private final user_Service user_service;
    @GetMapping("/getListUser")
    List<Users> getListUser()
    {
        return user_service.getListUser();
    }
    @GetMapping("/getUser/{userID}")
    Users getUsers(@PathVariable String userID)
    {
        return user_service.getUser(userID);
    }
    @GetMapping("/get_myInfo")
    Users get_myInfo()
    {
        return user_service.get_myInfo();
    }
}
