package lk.ac.kln.stu.shopping.auth.services;

import lk.ac.kln.stu.shopping.auth.jwtutils.TokenManager;
import lk.ac.kln.stu.shopping.auth.models.User;
import lk.ac.kln.stu.shopping.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenManager tokenManager;

    @Autowired
    public UserService(UserRepository userRepository, TokenManager tokenManager) {
        this.userRepository = userRepository;
        this.tokenManager = tokenManager;
    }

    public Optional<User> getUser(String token) {
        String email = this.tokenManager.getUsernameFromToken(token);
        return this.userRepository.findUserByEmail(email);
    }

}
