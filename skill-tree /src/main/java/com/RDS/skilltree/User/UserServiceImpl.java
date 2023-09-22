package com.RDS.skilltree.User;

import com.RDS.skilltree.Exceptions.NoEntityException;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
    }

    /**
     * Creates a new user.
     *
     * @param user the UserDRO object containing the user information
     */
    @Override
    public void createUser(UserDRO user) {
        try {
            UserModel userModel = UserDRO.toModel(user);
            userRepository.save(userModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a user with the specified ID using the provided user details.
     *
     * @param id                 the ID of the user to update
     * @param updatedUserDetails the updated details of the user
     */
    @Override
    @Transactional
    public void updateUser(UUID id, UserDRO updatedUserDetails) throws NoEntityException {
        Optional<UserModel> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty())
            throw new NoEntityException("User with id:" + id + " not found");
        UserModel user = userOptional.get();
        UserModel updatedUser = UserDRO.compareAndUpdateModel(user, updatedUserDetails);

        userRepository.save(updatedUser);
    }

    /**
     * Retrieves a user by their unique identifier.
     *
     * @param id the unique identifier of the user
     * @return the UserDTO representing the user
     */
    @Override
    public UserDTO getUserById(UUID id) throws NoEntityException {
        Optional<UserModel> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NoEntityException("User with id:" + id + " not found");
        }
        return UserDTO.toDTO(user.get());
    }

    /**
     * Retrieves a list of all users.
     *
     * @return a list of UserDTO objects representing the users
     */
    @Override
    public List<UserDTO> getAllUsers() throws NoEntityException {
        List<UserModel> usersData = userRepository.findAll();
        if (usersData.isEmpty())
            throw new NoEntityException("No users found");
        return usersData.stream().map(UserDTO::toDTO).toList();
    }

    /**
     * Adds a skill to a user.
     *
     * @param skillId the ID of the skill to be added
     * @param id      the ID of the user
     * @return none
     */
    @Override
    @Transactional
    public void addSkill(UUID skillId, UUID id) throws NoEntityException {
        Optional<SkillModel> skillOptional = skillRepository.findById(skillId);
        Optional<UserModel> userOptional = userRepository.findById(id);

        if (skillOptional.isPresent() && userOptional.isPresent()) {
            SkillModel skillModel = skillOptional.get();
            UserModel user = userOptional.get();

            skillModel.getUsers().add(user);
            skillRepository.save(skillModel);

            user.getSkills().add(skillModel);
            userRepository.save(user);
        } else {
            if (skillOptional.isEmpty())
                throw new NoEntityException("Skill with id:" + skillId + " not found");
            throw new NoEntityException("User with id:" + id + " not found");
        }
    }
}
