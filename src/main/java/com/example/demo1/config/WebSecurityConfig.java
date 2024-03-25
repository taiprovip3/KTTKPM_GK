package com.example.demo1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo1.service.UserDetailServicesImpl;
import com.example.demo1.util.JwtRequestFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private UserDetailServicesImpl userDetailServicesImpl;
	
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	
	@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailServicesImpl).passwordEncoder(passwordEncoder());
	}
	
	@Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/", "/register", "/login", "/hello", "/signout", "/users/**").permitAll().
                        anyRequest().authenticated().and()
                        .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and().sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler()).and()
//        .exceptionHandling().authenticationEntryPoint(new CustomHttp403ForbiddenEntryPoint());
        
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}