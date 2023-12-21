package com.RDS.skilltree.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDTO createUser(UserDRO user);

    void updateUser(UUID id,UserDRO user);

    UserDTO getUserById(UUID id);

    List<UserDTO> getAllUsers();

    void addSkill(UUID skill, UUID userId);
}