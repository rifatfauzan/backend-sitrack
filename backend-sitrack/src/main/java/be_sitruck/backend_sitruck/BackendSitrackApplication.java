package be_sitruck.backend_sitruck;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import be_sitruck.backend_sitruck.model.Role;
import be_sitruck.backend_sitruck.model.UserModel;
import be_sitruck.backend_sitruck.repository.RoleDb;
import be_sitruck.backend_sitruck.repository.UserDb;
import be_sitruck.backend_sitruck.restservice.UserRestService;
import jakarta.transaction.Transactional;

@SpringBootApplication
public class BackendSitrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendSitrackApplication.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner run(RoleDb roleDb, UserDb userDb, UserRestService userService){
		return args -> {
			UserModel user;

			if(roleDb.findByRole("Admin").orElse(null) == null) {
                Role role = new Role();
                role.setRole("Admin");
                roleDb.save(role);
            }

			if(roleDb.findByRole("Manager").orElse(null) == null){
				Role managerRole = new Role();
				managerRole.setRole("Manager");
				roleDb.save(managerRole);
			}

			if(roleDb.findByRole("Supervisor").orElse(null) == null){
				Role supervisorRole = new Role();
				supervisorRole.setRole("Supervisor");
				roleDb.save(supervisorRole);
			}

			if(roleDb.findByRole("Operasional").orElse(null) == null){
				Role operasionalRole = new Role();
				operasionalRole.setRole("Operasional");
				roleDb.save(operasionalRole);
			}

			if(roleDb.findByRole("Mekanik").orElse(null) == null){
				Role mekanikRole = new Role();
				mekanikRole.setRole("Mekanik");
				roleDb.save(mekanikRole);
			}

			if (userDb.findByUsername("admin") == null) {
				user = new UserModel();
				user.setUsername("admin");
				user.setPassword(userService.hashPassword("admin"));
				user.setRole(roleDb.findByRole("Admin").orElse(null));
				userDb.save(user);
			}
		};
	}

}
