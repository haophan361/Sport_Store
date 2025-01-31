package com.sport_store.Controller.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.AuthenticationDTO.authentication_request;
import com.sport_store.DTO.request.UserDTO.register_account;
import com.sport_store.DTO.response.authentication_response;
import com.sport_store.Entity.Tokens;
import com.sport_store.Entity.Users;
import com.sport_store.Service.*;
import com.sport_store.Util.LoadUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class user_Controller {
    private final user_Service user_service;
    private final token_Service token_service;
    private final mail_Service mail_service;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String client_id;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String client_secret;
    @Value("${jwt.valid-duration}")
    private long validDuration;
    private final authentication_Service authentication_service;
    private final LoadUser load_user;
    private final cookie_Service cookie_service;

    @GetMapping("/web/form_register")
    public String getForm_Register() {
        return "web/register";
    }

    @GetMapping("/web/form_login")
    public String getForm_Login() {
        return "web/login";
    }

    @GetMapping("/form/changePassword")
    public String getForm_changePassword() {
        return "user/changePassword";
    }

    @GetMapping("/form/mail_forgetPassword")
    public String getForm_mail_forgetPassword() {
        return "user/forgetPassword";
    }

    @GetMapping("/web/setToken_forgetPassword")
    public String setToken_ForgetPassword(@RequestParam("token_resetPassword") String token_resetPassword, HttpServletResponse httpServletResponse, Model model) throws ParseException, JOSEException {
        if (!authentication_service.isValidTokenRestPassword(token_resetPassword)) {
            List<String> messageError = new ArrayList<>();
            messageError.add("Token không hợp lệ");
            model.addAttribute("error_message", messageError);
            return "web/error";
        }
        httpServletResponse.addCookie(cookie_service.create_tokenResetPasswordCookie(token_resetPassword));
        return "user/setTokenResetPassword";
    }


    @GetMapping("/form/changeInfoUser")
    public String getForm_UpdateInfoUser(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            return "web/login";
        }
        String email = (String) session.getAttribute("email");
        Users user = user_service.getUserByEmail(email);
        model.addAttribute("user", user);
        return "user/changeInfoUser";
    }

    @GetMapping("/login/oauth2/google")
    public String authenticationGoogle(@RequestParam String code, HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse) {
        try {
            String accessToken = getAccessToken(code);
            Users user = getUserInfo(accessToken);
            Users userEntity;
            authentication_response response;
            if (user_service.existByEmail(user.getUser_email())) {
                userEntity = user_service.getUserByEmail(user.getUser_email());
                authentication_request request = authentication_request.builder()
                        .email(userEntity.getUser_email())
                        .password(userEntity.getUser_password())
                        .build();
                response = authentication_service.authenticate(request, true);
            } else {
                register_account registerAccount = register_account.builder()
                        .email(user.getUser_email())
                        .name(user.getUser_name())
                        .password(UUID.randomUUID().toString())
                        .phone(null)
                        .date_of_birth(null)
                        .build();
                response = authentication_service.authenticate_LoginOAuth2(registerAccount);
                mail_service.SendEmailRandomPassword(registerAccount.getEmail(), registerAccount.getPassword());
                Cookie firstLoginGoogle = load_user.firstLogin_WithGoogle();
                httpServletResponse.addCookie(firstLoginGoogle);
            }
            SignedJWT signedJWT = authentication_service.verifyToken(response.getToken(), false);
            Tokens tokensEntity = Tokens.builder()
                    .token_id(signedJWT.getJWTClaimsSet().getJWTID())
                    .user_token(response.getToken())
                    .token_expiration_time(LocalDateTime.now().plusHours(validDuration))
                    .user_email(user.getUser_email())
                    .build();
            token_service.createToken(tokensEntity);
            HttpSession session = httpServletRequest.getSession();
            load_user.userSession(session, response);
            Cookie cookie = cookie_service.create_tokenCookie(response.getToken());
            httpServletResponse.addCookie(cookie);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/web/form_login";
        }
    }

    public String getAccessToken(String authorizationCode) {
        String tokenUrl = "https://oauth2.googleapis.com/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("client_id", client_id);
        body.add("client_secret", client_secret);
        body.add("redirect_uri", "http://localhost:8080/login/oauth2/google");
        body.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

        // Use ObjectMapper to parse the JSON response and extract the access token
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText(); // Extract the access token
        } catch (Exception e) {
            return null; // or handle this as needed
        }
    }

    public Users getUserInfo(String accessToken) {
        String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            return Users.builder()
                    .user_name(jsonNode.get("name").asText())
                    .user_email(jsonNode.get("email").asText())
                    .build();
        } catch (Exception e) {
            return null;
        }
    }
}
