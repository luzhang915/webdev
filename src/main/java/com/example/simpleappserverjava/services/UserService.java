package com.example.simpleappserverjava.services;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.simpleappserverjava.models.User;
import com.example.simpleappserverjava.repositories.UserRepository;

@RestController
public class UserService {
	// http://localhost:8080/register
	
	@Autowired
	UserRepository userRepository;
	
	@PostMapping("/register")
	public User register(@RequestBody User user, HttpSession session) {
		User currentUser = userRepository.save(user);
		session.setAttribute("currentUser", currentUser);
		return currentUser;
	}
	
	@GetMapping("/profile")
	public Optional<User> profile(HttpSession session) {
		User currentUser = (User) session.getAttribute("currentUser");
		return userRepository.findById(currentUser.getId());
	}
	
//	@GetMapping("/checkLogin")
//	public User checkLogin(HttpSession session) {
//		return (User) session.getAttribute("currentUser");
//	}
	
//	@PostMapping("/login")
//	public User login(@RequestBody User user) {
//		return userRepository.findUserByCredentials(user.getUsername(), user.getPassword());
//	}
	
	@PostMapping("/login")
	public User login(@RequestBody User user, HttpSession session) {
		user = userRepository.findUserByCredentials(user.getUsername(), user.getPassword());
		session.setAttribute("currentUser", user);
		return user;
	}
	
	@PutMapping("/api/user/{userId}")
	public User updateUser(
			@PathVariable("userId") int id,
			@RequestBody User newUser) {
		Optional<User> optional = userRepository.findById(id);
		if(optional.isPresent()) {
			User user = optional.get();
			user.setFirstname(newUser.getFirstname());
			user.setLastname(newUser.getLastname());
			return userRepository.save(user);
		}
		return null;
	}
	
	@GetMapping("/api/user/{userId}")
	public Optional<User> findUserByUserId(@PathVariable("userId") String userId) {
		int id = Integer.parseInt(userId);
		return userRepository.findById(id);
	}
	
	@DeleteMapping("/api/user/{userId}")
	public void deleteUser(@PathVariable("userId") int id) {
		userRepository.deleteById(id);
	}
	
	@GetMapping("/api/user")
	public List<User> findAllUsers() {
		return (List<User>) userRepository.findAll();
	}
}
