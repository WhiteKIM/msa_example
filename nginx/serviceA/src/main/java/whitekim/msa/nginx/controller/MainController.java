package whitekim.msa.nginx.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping
    public String test() {
        return "call Service A";
    }
}
