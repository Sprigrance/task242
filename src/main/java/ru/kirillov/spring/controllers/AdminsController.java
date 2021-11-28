package ru.kirillov.spring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kirillov.spring.models.Role;
import ru.kirillov.spring.models.User;
import ru.kirillov.spring.services.RoleService;
import ru.kirillov.spring.services.UserService;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admins")
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminsController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    // index-страница после редиректа
    @GetMapping()
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/getAllUsers";
    }

    // введенный в адресной строке id попадет с помощью @PathVariable в int id
    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.getUser(id));
        return "admin/getUser";
    }

    // GET-запрос создаст модель "newUser" и поместит его как объект для создания в new.html
    @GetMapping("/new")
    public String createUser(@ModelAttribute("newUser") User user) {
        return "admin/new";
    }

    // POST-метод возьмет "newUser" со страницы new.html,
    //  передаст его с помощью @ModelAttribute("newUser") в User user,
    //   передаст с помощью checkbox, "newRoles?ROLE_ADMIN" и @RequestParam в String[] strRoles - строковые роли
    //    и сделает .saveUser().
    // Если никакого User модель содержать не будет, то в User user поместится user с полями по умолчанию (0, null, null)
    @PostMapping()
    public String addUser(@ModelAttribute("newUser") User user,
                          @RequestParam String[] strRoles) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Шифруем пароль
        user.setRoles(roleService.getSetRoleFromArray(strRoles));      // Преобразовываем роли
        userService.saveUser(user);
        return "redirect:/admins";
    }

    // GET-запрос со страницы getUser передаст id и перейдет в этот метод по адресу /{id}/edit
    // модель примет пользователя (+ его роли), найденного по id и откроет страницу editUser.html
    @GetMapping("/{id}/edit")
    public String editUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("existingUser", userService.getUser(id));
        return "admin/editUser";
    }

    // PATCH-запрос из editUser.html возьмет пользователя из модели existingUser, поместит его в user,
    //  id принимается из editUser.html с помощью @PathVariable.
    //   Далее произойдет изменение с помощью updateUser()
    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("existingUser") User user,
                             @PathVariable("id") int id,
                             @RequestParam String[] strRoles) {

        user.setRoles(roleService.getSetRoleFromArray(strRoles));      // Преобразовываем роли
        userService.updateUser(id, user);
        return "redirect:/admins";
    }

    // DELETE-запрос с id найдет пользователя, удалит его, и перейдет в admins/
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admins";
    }

//  Ручное добавление пользователей и ролей
//    @PostConstruct
//    public void myinit() {
//        Role role1 = new Role("ROLE_ADMIN");
//        Role role2 = new Role("ROLE_USER");
//        roleService.saveRole(role1);
//        roleService.saveRole(role2);
//        Set<Role> roles = new HashSet<>();
//        roles.add(role1);
//        roles.add(role2);
//
//        User user = new User();
//        user.setUsername("admin");
//        user.setPassword(passwordEncoder.encode("admin"));
//        user.setRoles(roles);
//
//        userService.saveUser(user);
//    }
}
