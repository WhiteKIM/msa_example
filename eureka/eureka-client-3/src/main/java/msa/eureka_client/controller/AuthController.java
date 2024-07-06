package msa.eureka_client.controller;

import lombok.RequiredArgsConstructor;
import msa.eureka_client.model.User;
import msa.eureka_client.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증, 인가 매핑 컨트롤러
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/info/{id}")
    public ResponseEntity<User> getUserInfo(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinUser(User user) {
        userService.save(user);

        return ResponseEntity.ok("회원가입 완료");
    }
}
