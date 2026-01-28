package com.kamis.enterprise_platform.controller;

import com.kamis.enterprise_platform.dto.CreateUserRequest;
import com.kamis.enterprise_platform.dto.UpdateUserRequest;
import com.kamis.enterprise_platform.dto.UserResponse;
import com.kamis.enterprise_platform.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService svc;

    public UserController(UserService svc) {
        this.svc = svc;
    }
    @GetMapping("/me")
    @PreAuthorize("hasAuthority('user:read')")
    public Map<String, Object> me(Authentication auth) {
        return Map.of(
                "username", auth.getName(),
                "authorities", auth.getAuthorities().stream().map(a -> a.getAuthority()).toList()
        );
    }

    @PreAuthorize("hasAuthority('user:read')")
    @GetMapping
    public Page<UserResponse> list(Pageable pageable) {
        return svc.list(pageable);
    }

    @PreAuthorize("hasAuthority('user:create')")
    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest req) {
        return svc.create(req);
    }

    @PreAuthorize("hasAuthority('user:update')")
    @PutMapping("/{id}")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest req) {
        return svc.update(id, req);
    }

    @PreAuthorize("hasAuthority('user:delete')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        svc.disable(id);
    }
}
