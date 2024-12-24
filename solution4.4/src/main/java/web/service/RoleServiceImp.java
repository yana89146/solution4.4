package web.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web.model.Role;
import web.repository.RoleRepository;

@Service
@Transactional
public class RoleServiceImp implements RoleService {

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public Role findByName(String name){
        Role role = null;
        for(Role r:roleRepository.findAll()){
            if(r.getName().equals(name.toUpperCase())){
                role=r;
            }
        }
        return role;
    }

    @Override
    public Role save(String name){
        Role r = new Role();
        r.setName(name);
        roleRepository.save(r);
        return r;
    }

    @Override
    public Role getJSONRole(Role role) {
            Role newRole = new Role();
        newRole.setName(role.getName());
        newRole.setId(role.getId());
        return newRole;
    }


}
