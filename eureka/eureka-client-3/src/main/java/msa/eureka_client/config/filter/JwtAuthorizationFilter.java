package msa.eureka_client.config.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import msa.eureka_client.config.auth.PrincipalDetail;
import msa.eureka_client.model.User;
import msa.eureka_client.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private String secret = "Secret1234";
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authToken = request.getHeader("Authorization");

        if(authToken.isBlank() || authToken.isEmpty()) {
            throw new RuntimeException();
        }

        Claims claims = Jwts
                .parser()
                .setSigningKey(secret)
                .parseClaimsJws(authToken)
                .getBody();

        if(claims != null) {
            User loginUser = userRepository.findByUsername(claims.getSubject()).orElseThrow();
            PrincipalDetail principalDetail = new PrincipalDetail(loginUser);
            UsernamePasswordAuthenticationToken accessToken = new UsernamePasswordAuthenticationToken(principalDetail, null, principalDetail.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(accessToken);
        }

        chain.doFilter(request, response);
    }
}
