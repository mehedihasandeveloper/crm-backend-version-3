package com.mehediFifo.CRM.authRepository;

import com.mehediFifo.CRM.authModel.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
