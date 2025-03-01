package be_sitruck.backend_sitruck.restservice;

import java.util.*;

import be_sitruck.backend_sitruck.model.Role;

public interface RoleRestService {
    List<Role> getAllRoles();
    Role getRoleByRoleName(String name);
}
