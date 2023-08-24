package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public void createUser(UserDRO user) {
        UserModel userModel = UserDRO.toModel(user);
        try {
            userRepository.save(userModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserModel updateUser(UserModel user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public UserModel getUserById(UUID id) {
        return userRepository.findById(id).get();
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserModel> usersData = userRepository.findAll();

        List<UserDTO> users = usersData.stream().map(user -> {
            UserDTO userDTO = UserDTO.toDTO(user);
            return userDTO;
        }).toList();
        return users;
    }

    @Override
    public String addSkill(UUID skillId, UUID id) {
        SkillModel skillModel = skillRepository.findById(skillId).get();
        UserModel user = userRepository.findById(id).get();
        skillModel.getUsers().add(user);
        skillRepository.save(skillModel);

        user.getSkills().add(skillModel);
        userRepository.save(user);

        return "Success";
    }
}
