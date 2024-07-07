package com.RDS.skilltree.User;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDRO user);

    void updateUser(String id, UserDRO user);

    UserDTO getUserById(String id);

    List<UserDTO> getAllUsers();

    void addSkill(Integer skill, String userId);
}
