package lk.ac.kln.stu.shopping.auth.services;

import jakarta.ws.rs.NotFoundException;
import lk.ac.kln.stu.shopping.auth.authentication.TokenManager;
import lk.ac.kln.stu.shopping.auth.models.Role;
import lk.ac.kln.stu.shopping.auth.models.User;
import lk.ac.kln.stu.shopping.auth.models.UserRequest;
import lk.ac.kln.stu.shopping.auth.repositories.RoleRepository;
import lk.ac.kln.stu.shopping.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenManager tokenManager;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, TokenManager tokenManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenManager = tokenManager;
    }

    public Optional<User> getUser(String token) {
        String email = this.tokenManager.getUsernameFromToken(token);
        return this.userRepository.findUserByEmail(email);
    }

    public void createUser(UserRequest userRequest) throws NotFoundException {
        Collection<Role> roles = new ArrayList<>();
        for (String roleName : userRequest.getRoleNames()) {
            Optional<Role> roleRecord = this.roleRepository.findRoleByName(roleName);
            if (roleRecord.isPresent()) {
                roles.add(roleRecord.get());
            }
            else {
                throw new NotFoundException("One of the roles is invalid!");
            }
        }

        User user = new User(userRequest.getFirstName(), userRequest.getLastName(), userRequest.getEmail(), userRequest.getPassword(), userRequest.getMobile(), roles);
        this.userRepository.save(user);
    }

}
