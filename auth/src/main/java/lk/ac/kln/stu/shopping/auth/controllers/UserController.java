package lk.ac.kln.stu.shopping.auth.controllers;

import jakarta.ws.rs.NotFoundException;
import lk.ac.kln.stu.shopping.auth.models.User;
import lk.ac.kln.stu.shopping.auth.models.UserRequest;
import lk.ac.kln.stu.shopping.auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping({"user", "user/"})
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public User getUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String headerAuthorization) {
        String jwtToken = headerAuthorization.substring(7);
        Optional<User> user = this.userService.getUser(jwtToken);
        // Since guaranteed to exist, we directly get the user.
        return user.get();
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createUser(@RequestBody UserRequest userRequest) {
        try {
            this.userService.createUser(userRequest);
            return ResponseEntity.ok(new HashMap<>());
        }

        catch (NotFoundException notFoundException) {
            return ResponseEntity.badRequest().body(
                    new HashMap<>() {{
                        put("errorMessage", notFoundException.getMessage());
                    }}
            );
        }
    }

}
