//package com.RDS.skilltree.User;
//
//import com.RDS.skilltree.Exceptions.NoEntityException;
//import com.RDS.skilltree.Skill.SkillModel;
//import com.RDS.skilltree.Skill.SkillRepository;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//public class UserServiceImpl implements UserService{
//    private final UserRepository userRepository;
//    private final SkillRepository skillRepository;
//    public UserServiceImpl(UserRepository userRepository, SkillRepository skillRepository){
//        this.userRepository = userRepository;
//        this.skillRepository = skillRepository;
//    }
//
//    @Override
//    public UserDTO createUser(UserDRO user) {
//        UserModel userModel = UserDRO.toModel(user);
//        userRepository.save(userModel);
//        return UserDTO.toDTO(userModel);
//    }
//
//    @Override
//    public void updateUser(UUID id, UserDRO user) {
//
//    }
//
//    @Override
//    public UserDTO getUserById(UUID id) {
//        Optional<UserModel> userModel = userRepository.findById(id);
//        return userModel.map(UserDTO::getUsersWithSkills).orElse(null);
//    }
//
//    @Override
//    public List<UserDTO> getAllUsers() {
//        return null;
//    }
//
//    /**
//     * updates the user and skill both
//     * @param skillId
//     * @param userId
//     */
//    @Override
//    @Transactional
//    public void addSkill(UUID skillId, UUID userId) {
//       Optional<UserModel> userOptional = userRepository.findById(userId);
//       Optional<SkillModel> skillOptional = skillRepository.findById(skillId);
//
//       if(userOptional.isPresent() && skillOptional.isPresent()){
//           UserModel userModel = userOptional.get();
//           SkillModel skillModel = skillOptional.get();
//
//           userModel.getSkills().add(skillModel);
//           skillModel.getUsers().add(userModel);
//
//           userRepository.save(userModel);
//           skillRepository.save(skillModel);
//       } else {
//           if(skillOptional.isEmpty()){
//               throw new NoEntityException("Skill Id is not passed in the input");
//           }
//           throw new NoEntityException("User with Id doesn't exists");
//       }
//    }
//}
