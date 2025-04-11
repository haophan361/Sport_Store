package com.sport_store.Controller.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;
import com.sport_store.DTO.request.authentication_Request.authentication_request;
import com.sport_store.DTO.request.customer_Request.register_account;
import com.sport_store.DTO.response.account_Response.authentication_response;
import com.sport_store.Entity.Accounts;
import com.sport_store.Entity.Customers;
import com.sport_store.Entity.Tokens;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class customer_Controller {
    private final customer_Service customer_service;
    private final token_Service token_service;
    private final mail_Service mail_service;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String google_client_id;
    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String google_client_secret;
    @Value("${jwt.valid-duration}")
    private long validDuration;
    private final authentication_Service authentication_service;
    private final LoadUser load_user;
    private final cookie_Service cookie_service;
    private final account_Service account_service;

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

    @GetMapping("/form/forgetPassword")
    public String getForm_ForgetPassword() {
        return "user/forgetPassword";
    }

    @GetMapping("/form/check_codeVerifyEmail")
    public String getForm_checkCodeResetPassword() {
        return "user/verify_Email";
    }

    @GetMapping("/form/request_forgetPassword")
    public String getForm_mail_forgetPassword() {
        return "user/request_forgetPassword";
    }

    @GetMapping("/form/changeInfoCustomer")
    public String getForm_UpdateInfoUser(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session == null) {
            return "web/login";
        }
        String email = (String) session.getAttribute("email");
        Customers customer = customer_service.getUserByEmail(email);
        model.addAttribute("customer", customer);
        return "user/changeInfoUser";
    }

    @GetMapping("/login/oauth2/google")
    public String authenticationGoogle(@RequestParam String code, HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse) {
        return authenticationOAuth2(code, "Google", httpServletRequest, httpServletResponse);
    }

    @Transactional
    public String authenticationOAuth2(String code, String provider, HttpServletRequest httpServletRequest,
                                       HttpServletResponse httpServletResponse) {
        try {
            String accessToken = getAccessToken(code, provider);
            JsonNode jsonNode = getUserInfo(accessToken, provider);
            authentication_response response;
            if (account_service.existByEmail(jsonNode.get("email").asText())) {
                Accounts account = account_service.getAccountByEmail(jsonNode.get("email").asText());
                authentication_request request = authentication_request.builder()
                        .email(account.getEmail())
                        .password(account.getPassword())
                        .build();
                response = authentication_service.authenticate(request, true);
            } else {
                register_account registerAccount = register_account.builder()
                        .email(jsonNode.get("email").asText())
                        .name(jsonNode.get("name").asText())
                        .password(UUID.randomUUID().toString())
                        .phone(null)
                        .date_of_birth(null)
                        .build();
                response = authentication_service.authenticate_LoginOAuth2(registerAccount);
                mail_service.SendEmailRandomPassword(registerAccount.getEmail(), registerAccount.getPassword());

                Cookie register_Google = cookie_service.create_Cookie("true", "register_Google", "/", -1, false);
                httpServletResponse.addCookie(register_Google);
            }
            SignedJWT signedJWT = authentication_service.verifyToken(response.getToken(), false);
            Tokens tokensEntity = Tokens.builder()
                    .token_id(signedJWT.getJWTClaimsSet().getJWTID())
                    .user_token(response.getToken())
                    .token_expiration_time(LocalDateTime.now().plusHours(validDuration))
                    .user_email(jsonNode.get("email").asText())
                    .build();
            token_service.createToken(tokensEntity);
            HttpSession session = httpServletRequest.getSession();
            load_user.CustomerSession(session, response);
            Cookie cookie = cookie_service.create_Cookie(response.getToken(), "token", "/", 3600, true);
            httpServletResponse.addCookie(cookie);
            account_service.Update_isOnline(jsonNode.get("email").asText(), true);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/web/form_login";
        }
    }

    public String getAccessToken(String authorizationCode, String provider) {
        String tokenUrl = "";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("code", authorizationCode);
        if (provider.equals("Google")) {
            tokenUrl = "https://oauth2.googleapis.com/token";
            body.add("client_id", google_client_id);
            body.add("client_secret", google_client_secret);
            body.add("redirect_uri", "http://localhost:8080/login/oauth2/google");
            body.add("grant_type", "authorization_code");
        }
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

    public JsonNode getUserInfo(String accessToken, String provider) {

        String userInfoUrl = "";
        if (provider.equals("Google")) {
            userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, entity, String.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(response.getBody());
        } catch (Exception e) {
            return null;
        }
    }
}
