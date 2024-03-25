package com.example.demo1.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo1.entity.Token;
import com.example.demo1.entity.User;
import com.example.demo1.entity.UserDTO;
import com.example.demo1.entity.UserPrincipal;
import com.example.demo1.service.TokenServices;
import com.example.demo1.service.UserServices;
import com.example.demo1.util.JwtUltility;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/")
public class RestController {
	
	@Autowired
	private UserServices userServices;
	
	@Autowired
	private JwtUltility jwtUltility;

	@Autowired
	private TokenServices tokenServices;
	
	@Autowired
	private AuthenticationManager authenticationManagerBean;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/hello")
	public String helloWorld() {
		return "Hello World";
	}
	
	@PostMapping("/register")
	public User registerUser(@RequestBody UserDTO u) {
		return userServices.saveUser(u);
	}
	
	@PostMapping("/login")
	@CachePut(value = "users", key = "'allUsers'")
	public ResponseEntity<?> signInUser(@RequestBody UserDTO u){
		
		UserPrincipal userPrincipal = userServices.getUserByUsername(u.getUsername());
		if(userPrincipal == null || !passwordEncoder.matches(u.getPassword(), userPrincipal.getPassword()))
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Account password is wrong!");
		try {
			Authentication authentication = authenticationManagerBean.authenticate(new UsernamePasswordAuthenticationToken(u.getUsername(), u.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			System.out.println("\n\n>> User: `" + u.getUsername() + "` sign in successfully!\n\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean isTokenUserExisted = tokenServices.checkTokenUser(u.getUsername());
		Token token = new Token();
		String tokenString = jwtUltility.generateHS512Token(userPrincipal);
		token.setToken(tokenString);
		token.setTokenExpDate(jwtUltility.getExpirationDateFromToken(tokenString));
		token.setUser(userServices.getUserByUserName(u.getUsername()));
		if(isTokenUserExisted)
			tokenServices.updateToken(token);
		else
			tokenServices.saveToken(token);
		return ResponseEntity.ok(token.getToken());
	}
	
	@GetMapping("/signout")
	public String signOut() {
		SecurityContextHolder.getContext();
		return "\n\n(!) Sign out account success\n\n";
	}
	
	// Cache
	@GetMapping("/users/generate")
	public String generatePlant() {
		for(int i=1; i<5000; i++) {
			UserDTO userDTO = new UserDTO();
			userDTO.setUsername("taiproduaxe"+i);
			userDTO.setPassword("123123az");
			userServices.saveUser(userDTO);
		}
		return "Generated success.";
	}
	
	@GetMapping("/users")
	public List<User> getUsers(){
		return userServices.getUsers();
	}
}