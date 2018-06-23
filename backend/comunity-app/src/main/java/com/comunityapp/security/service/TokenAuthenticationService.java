
package com.comunityapp.security.service;

import com.comunityapp.security.other.UserDetailsAdapter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationService {
    private static final long EXPIRATIONTIME = 864_000_000_000L; // 10 days
    private static final String SECRET = "ThisIsASecret";
    private static final String TOKEN_PREFIX = "Bearer";
    private static final String HEADER_STRING = "Authorization";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CityAppUserDetailsService cityAppUserDetailsService;

    public void addAuthentication(HttpServletResponse res, String username, String role, String fullName, long id) {

        logger.info("addAuthentication:{},{}", username, role);

        String jwt = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
                .signWith(SignatureAlgorithm.HS512, SECRET).compact();
        JSONObject json = new JSONObject();
        try {
            json.put("Role", role);
            json.put("Token", jwt);
            json.put("FullName", fullName);
            json.put("ID", id);
        } catch (JSONException e) {
            logger.info("TokenAuthenticationService addAuthentication", e);
            logger.error(e.getMessage());
        }
        logger.info("JSON: {} ", json);
        try {
            res.getWriter().write(json.toString());
        } catch (IOException e) {
            logger.info("TokenAuthenticationService addAuthentication", e);
        }
        logger.trace("Token:{}", jwt);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token != null) {
            // parse the token.
            String userName = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody().getSubject();
            logger.info("request from : {}", userName);
            UserDetailsAdapter user = (UserDetailsAdapter) cityAppUserDetailsService.loadUserByUsername(userName);
            logger.info("user details:{} {}", user.getAuthorities(), user.getUsername());
            return user != null
                    ? new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
                    user.getAuthorities())
                    : null;
        }
        return null;
    }
}
