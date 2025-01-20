package com.appliance_store.Util;

import com.appliance_store.DTO.response.authentication_response;
import com.appliance_store.Entity.Users;
import com.appliance_store.Service.user_Service;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Load_userSession
{
    private final user_Service user_service;
    public void userSession(HttpSession session, authentication_response response)
    {
        if(response!=null)
        {
            Users user=user_service.getUserByEmail(response.getUser_response().getEmail());
            if(user!=null)
            {
                session.setAttribute("email", user.getUser_email());
                session.setAttribute("name", user.getUser_name());
                session.setAttribute("role", user.getUser_role());
                session.setAttribute("isLoggedIn",1);
                session.setAttribute("token",response.getToken());
            }
            else
            {
                session.setAttribute("isLoggedIn",0);
            }
        }
    }

}
