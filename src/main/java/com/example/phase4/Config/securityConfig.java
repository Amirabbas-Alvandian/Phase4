package com.example.phase4.Config;

import com.example.phase4.exception.EntityNotFoundException;
import com.example.phase4.repository.UserRepository;
import com.example.phase4.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class securityConfig {

    private final UserRepository userRepository;
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public securityConfig(UserRepository userRepository, UserService userService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       return http
               .cors(AbstractHttpConfigurer::disable)
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(u -> u.requestMatchers("/customer/*")
                       .hasRole("CUSTOMER"))
               .authorizeHttpRequests(a -> a.requestMatchers("/admin/*")
                       .hasRole("ADMIN"))
               .authorizeHttpRequests(s -> s.requestMatchers("/specialist/*")
                       .hasRole("SPECIALIST"))
               .authorizeHttpRequests(sub -> sub.requestMatchers("/subCategory/*").permitAll())
               .authorizeHttpRequests(cat -> cat.requestMatchers("/category/*").permitAll())
               .authorizeHttpRequests(customerRegister -> customerRegister.requestMatchers("/register/customer").permitAll())
               .authorizeHttpRequests(specialistRegister -> specialistRegister.requestMatchers("/register/specialist").permitAll())
               .authorizeHttpRequests(specialistRegister -> specialistRegister.requestMatchers("/register/admin").permitAll())
               .authorizeHttpRequests(user -> user.requestMatchers("/register/confirm-user").permitAll())
               .authorizeHttpRequests(user -> user.requestMatchers("/register/temp").permitAll())

               .httpBasic(withDefaults())
               //.exceptionHandling(AbstractHttpConfigurer::disable)
               .build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userService::findByEmail
        ).passwordEncoder(passwordEncoder);
    }

}
