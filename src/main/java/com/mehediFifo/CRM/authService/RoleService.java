package com.mehediFifo.CRM.authService;

import com.mehediFifo.CRM.authDTO.RoleDto;
import com.mehediFifo.CRM.authModel.Role;
import com.mehediFifo.CRM.authRepository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public String create(final RoleDto RoleDto) {
        final Role Role = new Role();
        mapToEntity(RoleDto, Role);
        Role.setRoleName(RoleDto.getRoleName());
        return roleRepository.save(Role).getRoleName();
    }
    private Role mapToEntity(final RoleDto RoleDto, final Role Role) {
        Role.setRoleDescription(RoleDto.getRoleDescription());
        Role.setActive(RoleDto.getActive());
        return Role;
    }
}
