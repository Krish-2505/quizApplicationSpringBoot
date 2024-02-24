package com.example.quizproject;


import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
@Configuration
@EnableWebSecurity
public class securityconfig{
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource){
        return new JdbcUserDetailsManager(dataSource);
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
//        requestCache.setMatchingRequestParameterName("login");
        http.authorizeHttpRequests((authorize)-> authorize.requestMatchers("/home").permitAll().requestMatchers("/register/**").permitAll().requestMatchers("/admin/**").hasRole("admin").requestMatchers("/teacher/**").hasRole("teacher").requestMatchers("/student/**").hasRole("student").anyRequest().authenticated()).formLogin((form)->form.loginPage("/login_page").loginProcessingUrl("/authenticateTheUser").defaultSuccessUrl("/dashboard",true).permitAll()).logout((logout)->logout.logoutSuccessUrl("/login_page"));
//        http
//                // ...
//                .requestCache((cache) -> cache
//                        .requestCache(requestCache)
//                );
        return http.build();
    }

}
