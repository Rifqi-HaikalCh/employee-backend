package net.javaguides.springboot.config;

import net.javaguides.springboot.security.JwtAuthenticationEntryPoint;
import net.javaguides.springboot.security.JwtRequestFilter;
import net.javaguides.springboot.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // Configure AuthenticationManager to use UserDetails service and PasswordEncoder
        auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        // Configure HttpSecurity as needed
        httpSecurity.csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/users/register", "/api/v1/users/login").permitAll() // Allow access to register and login
                .antMatchers("/api/v1/users/profile").hasAnyRole("USER", "SUPER_ADMIN", "STAFF_ADMIN", "CONTROL_ADMIN") // Profile accessible to all roles
                .antMatchers("/api/v1/employees/**").hasAnyRole("SUPER_ADMIN", "STAFF_ADMIN") // Employee list accessible to Super Admin and Staff Admin
                .antMatchers("/api/v1/roles/**").hasAnyRole("SUPER_ADMIN", "CONTROL_ADMIN") // Role management accessible to Super Admin and Control Admin
                .antMatchers("/dashboard").permitAll() // Allow access to dashboard
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
