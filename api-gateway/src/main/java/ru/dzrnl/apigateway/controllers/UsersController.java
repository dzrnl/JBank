package ru.dzrnl.apigateway.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.dzrnl.apigateway.dto.AuthRequest;
import ru.dzrnl.apigateway.services.BankGatewayService;
import ru.dzrnl.apigateway.utils.ProxyUtil;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final ProxyUtil proxyUtil;
    private final BankGatewayService gatewayService;

    public UsersController(ProxyUtil proxyUtil, BankGatewayService gatewayService) {
        this.proxyUtil = proxyUtil;
        this.gatewayService = gatewayService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@RequestBody JsonNode jsonNode, HttpServletRequest request) {
        try {
            AuthRequest authRequest = new ObjectMapper().treeToValue(jsonNode, AuthRequest.class);

            String body = jsonNode.toString();

            ResponseEntity<String> response = proxyUtil.forwardRequest(request, "/api/users", HttpMethod.POST, body);

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode userFromBank = new ObjectMapper().readTree(response.getBody());
                long userId = userFromBank.get("id").asLong();

                gatewayService.createClient(userId, authRequest.getLogin(), authRequest.getPassword());
            }
            return response;

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON");
        }
    }

    @PostMapping(path = "/admin", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createAdmin(@RequestBody JsonNode jsonNode) {
        try {
            AuthRequest authRequest = new ObjectMapper().treeToValue(jsonNode, AuthRequest.class);

            gatewayService.createAdmin(authRequest.getLogin(), authRequest.getPassword());

            return ResponseEntity.ok().build();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid JSON");
        }
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<String> getUser(@PathVariable Long userId, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/users/" + userId, HttpMethod.GET, null);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getAllUsers(@RequestParam(required = false) String gender,
                                              @RequestParam(required = false) String hairColor,
                                              HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/users", HttpMethod.GET, null);
    }

    @PostMapping("/{userId}/friends/{friendId}")
    @PreAuthorize("#userId == authentication.principal.userId")
    public ResponseEntity<String> addFriend(@PathVariable Long userId, @PathVariable Long friendId, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/users/" + userId + "/friends/" + friendId, HttpMethod.POST, null);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @PreAuthorize("#userId == authentication.principal.userId")
    public ResponseEntity<String> removeFriend(@PathVariable Long userId, @PathVariable Long friendId, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/users/" + userId + "/friends/" + friendId, HttpMethod.DELETE, null);
    }

    @GetMapping("/{userId}/friends")
    @PreAuthorize("#userId == authentication.principal.userId or hasRole('ADMIN')")
    public ResponseEntity<String> getUserFriends(@PathVariable Long userId, HttpServletRequest request) {
        return proxyUtil.forwardRequest(request, "/api/users/" + userId + "/friends", HttpMethod.GET, null);
    }
}
