package com.mehediFifo.CRM.security.services;

import com.mehediFifo.CRM.entity.Agent;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserDetailsImplAgent implements UserDetails {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String agentId;
    private String password;
    private Boolean status;

    public UserDetailsImplAgent(Long id, String agentId, String password, Boolean status) {
        this.id = id;
        this.agentId = agentId;
        this.password = password;
        this.status = status;
    }

    public static UserDetailsImplAgent build(Agent agent) {
        return new UserDetailsImplAgent(
                agent.getId(),
                agent.getAgentId(),
                agent.getPassword(),
                agent.getStatus()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // Agents might not have roles, adjust as needed.
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return agentId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }
}
