package com.mehediFifo.CRM.authController;

import com.mehediFifo.CRM.authDTO.*;
import com.mehediFifo.CRM.authModel.User;
import com.mehediFifo.CRM.authRepository.UserRepository;
import com.mehediFifo.CRM.authService.UserService;
import com.mehediFifo.CRM.repository.AgentRepository;
import com.mehediFifo.CRM.security.jwt.JwtUtils;
import com.mehediFifo.CRM.security.services.UserDetailsImpl;
import com.mehediFifo.CRM.security.services.UserDetailsImplAgent;
import com.mehediFifo.CRM.service.AgentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
//@CrossOrigin(origins = http://localhost:4200", allowCredentials = "true")
@CrossOrigin(origins = {"http://43.231.78.77:5011", "http://crm.fifo-tech.com:5011", "http://localhost:4200", "https://crm.fifo-tech.com"},  allowCredentials = "true")

public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AgentRepository agentRepository;
    @Autowired
    AgentService agentService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;



    @PostMapping(value = "/signInSuperAdmin", produces = "application/json")
    public ResponseEntity<?> authenticateAdmin(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        String token = jwtUtils.generateJwtToken(userDetails);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new JwtResponse(token));
    }



    @PostMapping("/signInAgent")
    public ResponseEntity<?> authenticateAgent(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImplAgent userDetails = (UserDetailsImplAgent) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(userDto.getUsername(),
                userDto.getEmail(),
                encoder.encode(userDto.getPassword()),
                userDto.getUserFirstName(),
                userDto.getUserLastName());

        userDto.setRoles(Collections.singleton("ADMIN"));
        try {
            userService.create(userDto);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse("one of role not found"));
        }

        return ResponseEntity.ok(new MessageResponse("Admin registered successfully!"));
    }

    @PostMapping("/signupqc")
    public ResponseEntity<?> registerQcUser(@Valid @RequestBody UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(userDto.getUsername(),
                userDto.getEmail(),
                encoder.encode(userDto.getPassword()),
                userDto.getUserFirstName(),
                userDto.getUserLastName());

        userDto.setRoles(Collections.singleton("QC"));
        try {
            userService.create(userDto);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse("one of role not found"));
        }

        return ResponseEntity.ok(new MessageResponse("Qc Inspector registered successfully!"));
    }
}
