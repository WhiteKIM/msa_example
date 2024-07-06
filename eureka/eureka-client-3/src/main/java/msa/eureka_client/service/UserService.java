package msa.eureka_client.service;

import msa.eureka_client.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void save(User user);
    User getUser(Long id);
    User login(User loginInfo);
}
