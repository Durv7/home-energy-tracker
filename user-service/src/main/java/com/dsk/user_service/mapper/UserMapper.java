package com.dsk.user_service.mapper;


import com.dsk.user_service.User;
import com.dsk.user_service.dto.UserCreateDTO;
import com.dsk.user_service.dto.UserResponseDTO;
import com.dsk.user_service.dto.UserUpdateDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring" ,
nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "createdAt" , expression = "java(java.time.Instant.now())")
    User toEntity(UserCreateDTO userCreateDTO);


    UserResponseDTO toResponse(User user);

    void updateUser(@MappingTarget User user , UserUpdateDTO userUpdateDTO);

    @AfterMapping
    default UserResponseDTO generateFullName(@MappingTarget UserResponseDTO userResponseDTO , User user){
        userResponseDTO.setFullName(user.getName()+" "+user.getSurname());
        return userResponseDTO;
    }
}
