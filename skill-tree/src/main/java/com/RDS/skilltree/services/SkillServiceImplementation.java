package com.RDS.skilltree.services;

import com.RDS.skilltree.models.Skill;
import com.RDS.skilltree.repositories.SkillRepository;
import com.RDS.skilltree.viewmodels.SkillViewModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImplementation implements SkillService {
    private final SkillRepository skillRepository;

    @Override
    public List<SkillViewModel> getAll() {
        return skillRepository.findAll().stream()
                .map(this::toViewModel)
                .collect(Collectors.toList());
    }

    public SkillViewModel toViewModel(Skill entity) {
        SkillViewModel viewModel = new SkillViewModel();
        BeanUtils.copyProperties(entity, viewModel);
        return viewModel;
    }
}
