package be_sitruck.backend_sitruck.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import be_sitruck.backend_sitruck.model.UserModel;

@Repository
public interface UserDb extends JpaRepository<UserModel, Long> {
    UserModel findByUsername(String username);
    Boolean existsByUsername(String username);
    UserModel findByUsernameIgnoreCase(String username);
    boolean existsByUsernameIgnoreCase(String username);
    List<UserModel> findByRole_IdIn(List<Long> roleIds);
    List<UserModel> findByRole_Id(Long roleId);
}
