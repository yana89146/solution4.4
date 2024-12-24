package web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import web.model.Role;
import web.model.User;
import web.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
public class UserServiceImp implements UserDetailsService, UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;


    @Autowired

    public UserServiceImp(@Lazy PasswordEncoder passwordEncoder, @Lazy UserService userService, RoleService roleService, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }


    @Override
    public void add(User user) {
        if (!user.getRoles().isEmpty()) {
            for (Role role : user.getRoles()) {
                if (roleService.findByName(role.getName()) == null) {
                    userRepository.save(user);
                    System.out.println("сохранена новая роль");
                } else {
                    User newUser = new User();
                    userRepository.save(newUser);
                    userService.updateById(user, newUser.getId());
                    System.out.println("сохранена старая роль");
                }
            }

        } else {
            User newUser = new User();
            userRepository.save(newUser);
            userService.updateById(user, newUser.getId());
            System.out.println("сохранен без ролей");
        }
    }


    @Override
    public User addWithoutRoles(User user) {
        User newUser = new User();
        userRepository.save(newUser);
        newUser.setRoles(new HashSet<>());
        newUser.setUsername(user.getUsername());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setEmail(user.getEmail());
        newUser.setAge(user.getAge());
        userService.updateById(newUser, newUser.getId());
        System.out.println(newUser.getId() + "это айди из метода");
        return newUser;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<User> newUsers = new ArrayList<>();
        for (User user : users) {
            User newUser = userService.getJSONUser(user);
            newUsers.add(newUser);
        }
        return newUsers;
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> user = userRepository.findById(id);
        user.get().getRoles().clear();
        userRepository.deleteById(id);
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void updateById(User user, Long id) {
        System.out.println("updated");
        User updatedUser = userRepository.findById(id).get();
        updatedUser.getRoles().clear();
        updatedUser.setRoles(user.getRoles());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setEmail(user.getEmail());
        updatedUser.setAge(user.getAge());
        userRepository.save(updatedUser);
    }

    @Override
    public User getJSONUser(User user) {
        User newUser = new User();
        newUser.setId(Long.parseLong(user.getId().toString()));
        newUser.setUsername(user.getUsername());
        newUser.setLastName(user.getLastName());
        newUser.setPassword(user.getPassword());
        newUser.setEmail(user.getEmail());
        newUser.setAge(Integer.parseInt(user.getAge().toString()));

        for (Role role : user.getRoles()) {
            newUser.setRole(new Role(role.getName()));
        }
        return newUser;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> optionalUser = Optional.ofNullable(userService.findByUsername(username));
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        User user = optionalUser.get();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities(user.getRoles()));

    }

    private Set<GrantedAuthority> authorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());
    }
}
