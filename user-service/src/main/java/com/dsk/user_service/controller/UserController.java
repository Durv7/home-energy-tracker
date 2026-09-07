package com.dsk.user_service.controller;

import com.dsk.user_service.dto.UserCreateDTO;
import com.dsk.user_service.dto.UserResponseDTO;
import com.dsk.user_service.dto.UserUpdateDTO;
import com.dsk.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateDTO userCreateDTO){

        UserResponseDTO userResponseDTO  = userService.createUser(userCreateDTO);

        return new ResponseEntity<>(userResponseDTO , HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable long id){

        UserResponseDTO userResponseDTO = userService.getUser(id);

        return new ResponseEntity<>(userResponseDTO , HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable long id , @RequestBody UserUpdateDTO userUpdateDTO){
        UserResponseDTO userResponseDTO  = userService.updateUser(id, userUpdateDTO);

        return new ResponseEntity<>(userResponseDTO , HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
