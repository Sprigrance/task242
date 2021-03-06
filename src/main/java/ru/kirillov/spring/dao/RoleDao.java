package ru.kirillov.spring.dao;

import ru.kirillov.spring.models.Role;

import java.util.List;

public interface RoleDao {
    List<Role> getAllRoles();
    Role getRoleByName(String role);
    void saveRole(Role role);
}
