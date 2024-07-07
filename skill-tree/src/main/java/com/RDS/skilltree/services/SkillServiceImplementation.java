package com.RDS.skilltree.services;

import com.RDS.skilltree.User.JwtUserModel;
import com.RDS.skilltree.User.UserModel;
import com.RDS.skilltree.User.UserRepository;
import com.RDS.skilltree.exceptions.SkillAlreadyExistsException;
import com.RDS.skilltree.exceptions.UserNotFoundException;
import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.viewmodels.CreateSkillViewModel;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SkillServiceImplementation implements SkillService {
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;

    @Override
    public List<SkillViewModel> getAll() {
        return skillRepository.findAll().stream()
                .map(SkillViewModel::toViewModel)
                .collect(Collectors.toList());
    }

    @Override
    public SkillViewModel create(CreateSkillViewModel skill) {
        if (skillRepository.existsByName(skill.getName())) {
            throw new SkillAlreadyExistsException(
                    String.format("Skill with name %s already exists", skill.getName()));
        }

        JwtUserModel jwtDetails =
                (JwtUserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // TODO : user the userId from jwtDetails after the login api is implemented
        String userId = "ae7a6673c5574140838f209de4c644fc";
        Optional<UserModel> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserNotFoundException("unable to create skill for the current user");
        }

        Skill newSkill = toEntity(skill);
        newSkill.setCreatedBy(user.get());

        return SkillViewModel.toViewModel(skillRepository.saveAndFlush(newSkill));
    }

    private Skill toEntity(CreateSkillViewModel viewModel) {
        Skill entity = new Skill();
        BeanUtils.copyProperties(viewModel, entity);
        return entity;
    }
}
