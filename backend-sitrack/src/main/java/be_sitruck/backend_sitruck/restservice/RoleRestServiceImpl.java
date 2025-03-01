package be_sitruck.backend_sitruck.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import be_sitruck.backend_sitruck.model.Role;
import be_sitruck.backend_sitruck.repository.RoleDb;
@Service
public class RoleRestServiceImpl implements RoleRestService{
    @Autowired
    private RoleDb roleDb;

    @Override
    public List<Role> getAllRoles() {
        return roleDb.findAll();
    }

    @Override
    public Role getRoleByRoleName(String name) {
        return roleDb.findByRole(name).orElse(null);
    }
}
