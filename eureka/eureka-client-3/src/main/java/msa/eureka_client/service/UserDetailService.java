package msa.eureka_client.service;

import lombok.RequiredArgsConstructor;
import msa.eureka_client.config.auth.PrincipalDetail;
import msa.eureka_client.model.User;
import msa.eureka_client.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if(optionalUser.isEmpty())
            throw new RuntimeException();

        return new PrincipalDetail(optionalUser.get());
    }
}
