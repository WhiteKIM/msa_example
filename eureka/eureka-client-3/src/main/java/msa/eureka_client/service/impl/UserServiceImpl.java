package msa.eureka_client.service.impl;

import lombok.RequiredArgsConstructor;
import msa.eureka_client.model.User;
import msa.eureka_client.repository.UserRepository;
import msa.eureka_client.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public User login(User loginInfo) {
        userRepository.findByUsername(loginInfo.getUsername());

        return null;
    }
}
