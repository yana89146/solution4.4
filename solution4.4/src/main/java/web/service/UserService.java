package web.service;

import web.model.User;

import java.util.List;

public interface UserService  {


    void add(User user);

    User addWithoutRoles(User user);

    List<User> getAllUsers();

    void deleteById(Long id);

    User findById(Long id);

    void updateById(User user, Long id);

    User getJSONUser(User user);

    User findByUsername(String username);
}
