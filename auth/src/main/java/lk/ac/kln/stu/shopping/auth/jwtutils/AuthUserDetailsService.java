package lk.ac.kln.stu.shopping.auth.jwtutils;

import lk.ac.kln.stu.shopping.auth.models.User;
import lk.ac.kln.stu.shopping.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException("No such user exists"));
    }

//    public void createUser(UserDetails user) {
//        this.userRepository.save((User) user);
//    }

}
