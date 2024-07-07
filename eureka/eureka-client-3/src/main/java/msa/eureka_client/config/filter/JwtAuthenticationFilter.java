package msa.eureka_client.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import msa.eureka_client.config.auth.PrincipalDetail;
import msa.eureka_client.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private String secret = "Secret1234";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            System.out.println("로그인 진행");
            ObjectMapper om = new ObjectMapper();
            User member = om.readValue(request.getInputStream(), User.class);
            System.out.println(member);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());

            Authentication authentication =
                    authenticationManager.authenticate(usernamePasswordAuthenticationToken);

            PrincipalDetail principalDetails = (PrincipalDetail) authentication.getPrincipal();
            System.out.println("로그인 완료 " + principalDetails.getUsername());//로그인이 정상적으로 이루어졌다는 의미

            return authentication;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        PrincipalDetail principalDetails = (PrincipalDetail) authResult.getPrincipal();
        long now = (new Date()).getTime();

        String jwtToken = Jwts.builder()
                .setSubject(principalDetails.getUsername())
                .claim("auth", principalDetails.getAuthorities())
                .setExpiration(new Date(now + 60000))
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        response.addHeader("Authorization", jwtToken);
    }
}
