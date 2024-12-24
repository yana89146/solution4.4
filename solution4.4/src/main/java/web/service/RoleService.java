package web.service;

import org.springframework.stereotype.Service;
import web.model.Role;

@Service
public interface RoleService {

    Role getJSONRole(Role role);
    Role findByName(String name);
    Role save(String name);

}
