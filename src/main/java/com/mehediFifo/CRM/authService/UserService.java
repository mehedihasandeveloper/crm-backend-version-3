package com.mehediFifo.CRM.authService;

import com.mehediFifo.CRM.authDTO.UserDto;
import com.mehediFifo.CRM.authModel.User;
import com.mehediFifo.CRM.authModel.Role;
import com.mehediFifo.CRM.authRepository.RoleRepository;
import com.mehediFifo.CRM.authRepository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Transactional
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;

    public String create(final UserDto userDTO) {

        final User user = new User();
        mapToEntity(userDTO, user);
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user).getUsername();
    }
    private User mapToEntity(final UserDto userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setUserFirstName(userDTO.getUserFirstName());
        user.setUserLastName(userDTO.getUserLastName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setEnabled(true);
        final List<Role> roles = roleRepository.findAllById(userDTO.getRoles() == null ? Collections.emptyList() : userDTO.getRoles());
        if (roles.size() != (userDTO.getRoles() == null ? 0 : userDTO.getRoles().size())) {
            throw new RuntimeException("one of the role not found");
        }
        user.setRoles(new HashSet<>(roles));
        return user;
    }
}
