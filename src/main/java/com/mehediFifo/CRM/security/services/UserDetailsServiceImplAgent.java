package com.mehediFifo.CRM.security.services;

import com.mehediFifo.CRM.entity.Agent;
import com.mehediFifo.CRM.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UserDetailsServiceImplAgent implements UserDetailsService {
    @Autowired
    private AgentRepository agentRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String agentId) throws UsernameNotFoundException {
        Agent agent = agentRepository.findByAgentId(agentId)
                .orElseThrow(() -> new UsernameNotFoundException("Agent Not Found with agentId: " + agentId));

        return UserDetailsImplAgent.build(agent);
    }
}
