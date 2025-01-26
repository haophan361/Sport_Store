package com.sport_store.Configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.text.ParseException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class CookieAuthenticationFilter extends OncePerRequestFilter {
    @Value("${jwt.signerKey}")
    private String signerKey;
    private final AuthenticationProviderConfig authenticationProviderConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {
        try {

            Cookie tokenCookie = WebUtils.getCookie(request, "token");
            if (tokenCookie != null) {
                String token = tokenCookie.getValue();
                JWSVerifier verifier = new MACVerifier(signerKey.getBytes());
                SignedJWT signedJWT = SignedJWT.parse(token);
                Date expiration_time = signedJWT.getJWTClaimsSet().getExpirationTime();
                var verified = signedJWT.verify(verifier);
                if (verified && expiration_time.after(new Date())) {
                    UserDetails userDetails = authenticationProviderConfig.getUserDetailsService()
                            .loadUserByUsername(signedJWT.getJWTClaimsSet().getSubject());
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken
                            (userDetails, null, userDetails.getAuthorities());
//                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    SecurityContextHolder.clearContext();
                }
            }
        } catch (ParseException | JOSEException e) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }
}
