package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.List;

@RestController
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;


    @Autowired
    public UserRestController(UserService userService,  RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @PostMapping(value = "/admin/allUsers")
    public ResponseEntity<User> add(@RequestBody User user) {
        userService.add(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }


    @PutMapping(value = "/admin/showList", produces = "application/json")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateById(user, user.getId());
        return new ResponseEntity(user, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/admin/showList", produces = "application/json")
    public ResponseEntity<User> deleteUser(@RequestBody User user) {
        userService.deleteById(user.getId());
        return new ResponseEntity(user, HttpStatus.CREATED);
    }


    @GetMapping(value = "/admin/allUsers", produces = "application/json")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users=userService.getAllUsers();
        return new ResponseEntity(users, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/oneRole/{name}", produces = "application/json")
    public ResponseEntity<Role> findRoleByName(@PathVariable String name) {
        Role rawRole= roleService.findByName(name);
        Role jsonRole= roleService.getJSONRole(rawRole);
        return new ResponseEntity(jsonRole, HttpStatus.OK);
    }


    @GetMapping(value = "/user/getOne/{id}", produces = "application/json")
    public ResponseEntity<User> one(@PathVariable Long id) {
        User user = userService.getJSONUser(userService.findById(id));
        return new ResponseEntity(user, HttpStatus.CREATED);

    }

    @GetMapping(value = "/user/getAuthUser/{name}", produces = "application/json")
    public ResponseEntity<User> one(@PathVariable String name) {
        User user = userService.getJSONUser(userService.findByUsername(name));
        return new ResponseEntity(user, HttpStatus.CREATED);

    }

}
