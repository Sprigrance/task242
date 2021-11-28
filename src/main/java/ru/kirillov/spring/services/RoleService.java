package ru.kirillov.spring.services;

import ru.kirillov.spring.models.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleByName(String role);
    void saveRole(Role role);
    Set<Role> getSetRoleFromArray(String[] roles);
}
