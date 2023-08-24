package com.RDS.skilltree.User;


import java.util.List;
import java.util.UUID;

public interface UserService {
    void createUser(UserDRO user);

    UserModel updateUser(UserModel user);

    UserModel getUserById(UUID id);

    List<UserDTO> getAllUsers();

    String addSkill(UUID skill, UUID id);
}