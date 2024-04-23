package com.example.bredexTest;

import com.example.bredexTest.user.model.User;
import com.example.bredexTest.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BredexTestApplication  implements CommandLineRunner {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(BredexTestApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		if (userRepository.findAll().isEmpty()){

			userRepository.save(User.builder().email("laci@laci.com").username("laci").password(passwordEncoder.encode("laci")).build());
			userRepository.save(User.builder().email("test@test.com").username("test").password(passwordEncoder.encode("test")).build());
		}

	}
}
