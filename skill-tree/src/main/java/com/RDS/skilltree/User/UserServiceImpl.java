package com.RDS.skilltree.User;

import com.RDS.skilltree.exceptions.NoEntityException;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.SkillRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    public UserServiceImpl(UserRepository userRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public UserDTO createUser(UserDRO user) {
        UserModel userModel = UserDRO.toModel(user);
        userRepository.save(userModel);
        return UserDTO.toDTO(userModel);
    }

    @Override
    public void updateUser(String id, UserDRO user) {}

    @Override
    public UserDTO getUserById(String id) {
        Optional<UserModel> userModel = userRepository.findById(id);
        return userModel.map(UserDTO::getUsersWithSkills).orElse(null);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return null;
    }

    /**
     * updates the user and skill both
     *
     * @param skillId
     * @param userId
     */
    @Override
    @Transactional
    public void addSkill(Integer skillId, String userId) {
        Optional<UserModel> userOptional = userRepository.findById(userId);
        Optional<Skill> skillOptional = skillRepository.findById(skillId);

        if (userOptional.isPresent() && skillOptional.isPresent()) {
            UserModel userModel = userOptional.get();
            Skill skill = skillOptional.get();

            //            userModel.getSkills().add(skillModel);
            //            skillModel.getUsers().add(userModel);

            userRepository.save(userModel);
            skillRepository.save(skill);
        } else {
            if (skillOptional.isEmpty()) {
                throw new NoEntityException("Skill Id is not passed in the input");
            }
            throw new NoEntityException("User with Id doesn't exists");
        }
    }
}
