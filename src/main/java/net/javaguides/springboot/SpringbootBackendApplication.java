package net.javaguides.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBackendApplication.class, args);
	}

}

//package net.javaguides.springboot;
//
//import net.javaguides.springboot.model.Role;
//import net.javaguides.springboot.model.User;
//import net.javaguides.springboot.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@SpringBootApplication
//public class SpringBootApplication implements CommandLineRunner {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private PasswordEncoder passwordEncoder;
//
//	public static void main(String[] args) {
//		SpringApplication.run(SpringBootApplication.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		if (userRepository.findByUsername("superadmin") == null) {
//			User superAdmin = new User();
//			superAdmin.setUsername("superadmin");
//			superAdmin.setEmail("superadmin@example.com");
//			superAdmin.setPassword(passwordEncoder.encode("superadmin123"));
//			superAdmin.setRole(Role.SUPER_ADMIN);
//			userRepository.save(superAdmin);
//		}
//	}
//}
