package com.papas.spring.security.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import com.papas.spring.security.security.ApplicationUserRole;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig{

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
   PasswordEncoder passwordEncoder;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws  Exception{
        http
                .csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "index", "/CSS/**", "/js/**").permitAll()
                        .requestMatchers("/students/**").hasRole(ApplicationUserRole.STUDENT.name())
//                        .requestMatchers(HttpMethod.DELETE,"/management/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                        .requestMatchers(HttpMethod.POST,"/management/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                        .requestMatchers(HttpMethod.PUT,"/management/**").hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                        .requestMatchers(HttpMethod.GET,"/management/**").hasAnyRole(ApplicationUserRole.ADMIN.name(), ApplicationUserRole.ADMINTRAINEE.name())
                        .anyRequest()
                        .authenticated()
                )
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)
                .and()
                .rememberMe() //default expiry time 2 weeks
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .key("somethingverysecured")
                .and()
                .logout()
                    .logoutUrl("/logout") // Use this syntax instead
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSION", "remember-me")
                    .logoutSuccessUrl("/login");
        //          .httpBasic();

        return http.build();


    }

    @Bean
    protected UserDetailsService userDetailsService(){
        UserDetails fredUser = User.builder()
                .username("Fred")
                .password(passwordEncoder.encode("password"))
//                .roles(ApplicationUserRole.STUDENT.name())
                .authorities(ApplicationUserRole.STUDENT.getGrantedAuthorities())
                .build();

        UserDetails paulUser = User.builder()
                .username("Paul")
                .password(passwordEncoder.encode("papa"))
//                .roles(ApplicationUserRole.ADMIN.name())
                .authorities(ApplicationUserRole.ADMIN.getGrantedAuthorities())
                .build();

        UserDetails felixUser = User.builder()
                .username("Felix")
                .password(passwordEncoder.encode("papa123"))
//                .roles(ApplicationUserRole.ADMINTRAINEE.name())
                .authorities(ApplicationUserRole.ADMINTRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                fredUser,
                paulUser,
                felixUser
        );
    }



}
