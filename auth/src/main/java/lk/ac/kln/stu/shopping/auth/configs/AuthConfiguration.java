package lk.ac.kln.stu.shopping.auth.configs;

import lk.ac.kln.stu.shopping.auth.jwtutils.AuthEntryPoint;
import lk.ac.kln.stu.shopping.auth.jwtutils.AuthRequestFilter;
import lk.ac.kln.stu.shopping.auth.jwtutils.AuthUserDetailsService;
import lk.ac.kln.stu.shopping.auth.models.Role;
import lk.ac.kln.stu.shopping.auth.models.User;
import lk.ac.kln.stu.shopping.auth.repositories.RoleRepository;
import lk.ac.kln.stu.shopping.auth.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true
)
public class AuthConfiguration {

    private static final List<String> SHOPPING_BASE_ROLES = List.of(
            "BUYER",
            "SELLER"
    );

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
            for (String roleName : AuthConfiguration.SHOPPING_BASE_ROLES) {
                if (roleRepository.findRoleByName(roleName).isEmpty()) {
                    roleRepository.save(new Role(roleName));
                }
            }

            // Add a buyer
            Role roleBuyer = roleRepository.findRoleByName("BUYER").get();
            if (userRepository.findUserByEmail("buyer@shopping.base").isEmpty()) {
                User buyer = new User("Buyer", "Person", "buyer@shopping.base", "abc123", "+94775156476", List.of(roleBuyer));
                userRepository.save(buyer);
            }

            // Add a Seller
            Role roleSeller = roleRepository.findRoleByName("SELLER").get();
            if (userRepository.findUserByEmail("seller@shopping.base").isEmpty()) {
                User seller = new User("Seller", "Person", "seller@shopping.base", "abc123", "+94712326576", List.of(roleSeller));
                userRepository.save(seller);
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthEntryPoint authenticationEntryPoint, AuthRequestFilter requestFilter) throws Exception {
        return http
                .csrf().disable()
                .httpBasic(Customizer.withDefaults())
                .authorizeRequests()
                    .requestMatchers("/login/**").permitAll()
//                    .requestMatchers("/seller").hasRole("SELLER")
//                    .requestMatchers("/buyer").hasRole("BUYER")
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(AuthUserDetailsService authUserDetailsService) {
        return authUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

}
