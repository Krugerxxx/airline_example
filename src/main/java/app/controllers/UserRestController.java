package app.controllers;

import app.entities.user.User;
import app.entities.user.UserDetailsImpl;
import app.services.interfaces.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Api("User API")
@Slf4j
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiOperation(value = "Get all users", tags = "user-rest-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "users found"),
            @ApiResponse(code = 204, message = "users not found")
    })
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("methodName: getAllUsers - get all users");
        var users = userService.getAllUsers();
        return users.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id", tags = "user-rest-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "user found"),
            @ApiResponse(code = 404, message = "user not found")
    })
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("methodName: getUserById - get user by id. id = {}", id);
        var user = userService.getUserById(id);
        return user.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NOT_FOUND)
                : new ResponseEntity<>(user.get(), HttpStatus.OK);
    }

    @GetMapping("/auth")
    @ApiOperation(value = "Get authenticated user", tags = "user-rest-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "user found")
    })
    public ResponseEntity<User> getAuthenticatedUser() {
        log.info("methodName: getAuthenticatedUser - get authenticated user");
        UserDetailsImpl details = (UserDetailsImpl) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return new ResponseEntity<>(userService.getUserByEmail(details.getUsername()),
                HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "Add user", tags = "user-rest-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "user added")
    })
    public ResponseEntity<User> addUser(@RequestBody User user) {
        log.info("methodName: addUser - add new user");
        userService.saveUser(user);
        return new ResponseEntity<>(userService.getUserByEmail(user.getEmail()), HttpStatus.CREATED);
    }

    @PatchMapping
    @ApiOperation(value = "Edit existed user", tags = "user-rest-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "user edited")
    })
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("methodName: updateUser - update user with id = {}", user.getId());
        userService.updateUser(user);
        return new ResponseEntity<>(userService.getUserByEmail(user.getEmail()), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete user by id", tags = "user-rest-controller")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "user deleted"),
            @ApiResponse(code = 404, message = "user not found")
    })
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) {
        log.info("methodName: deleteUserById - delete user with id = {}", id);
        var user = userService.getUserById(id);
        if (user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
