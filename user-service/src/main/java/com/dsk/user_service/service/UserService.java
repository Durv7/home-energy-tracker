package com.dsk.user_service.service;

import com.dsk.user_service.User;
import com.dsk.user_service.dto.UserCreateDTO;
import com.dsk.user_service.dto.UserResponseDTO;
import com.dsk.user_service.dto.UserUpdateDTO;
import com.dsk.user_service.exception.UserNotFoundException;
import com.dsk.user_service.mapper.UserMapper;
import com.dsk.user_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserResponseDTO createUser(UserCreateDTO userCreateDTO){
        User user =  userMapper.toEntity(userCreateDTO);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public UserResponseDTO getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        return userMapper.toResponse(user);
    }

    public UserResponseDTO updateUser(long id , UserUpdateDTO userUpdateDTO){

        User user = userRepository.findById(id)
                .orElseThrow(()-> new UserNotFoundException("User Not Found"));

        userMapper.updateUser(user , userUpdateDTO);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    public void deleteUser(long id){
        User user = userRepository.findById(id).orElseThrow(()->new UserNotFoundException("User Not Found"));
        user.setDeleted(true);
        userRepository.save(user);
    }


}
