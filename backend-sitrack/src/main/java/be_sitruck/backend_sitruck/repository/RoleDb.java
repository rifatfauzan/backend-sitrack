package be_sitruck.backend_sitruck.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be_sitruck.backend_sitruck.model.Role;

@Repository
public interface RoleDb extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(String role);
}
