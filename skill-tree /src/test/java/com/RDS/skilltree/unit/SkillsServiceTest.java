package com.RDS.skilltree.unit;

import com.RDS.skilltree.Skill.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SkillsServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    @Autowired
    private SkillsServiceImpl skillService;


    @Test
    public void testGetSkillById() {
        UUID skillId = UUID.randomUUID();
        SkillModel skillModel = SkillModel.builder().id(skillId).build();

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skillModel));

        SkillDTO result = skillService.getSkillById(skillId);
        assertNotNull(result);
        assertEquals("The skill Id doesn't matches the expected skillId", skillId, result.getId());
    }

    @Test
    public void testGetSkillsByName() {
        String skillName = "Java";
        SkillModel skillModel = SkillModel.builder()
                .name("Java").build();

        when(skillRepository.findByName(skillName)).thenReturn(Optional.of(skillModel));

        SkillDTO result = skillService.getSkillByName("Java");
        assertEquals(result.getName(), skillName);
    }

    @Test
    public void testGetAllSkills(){
        SkillModel skillJava = SkillModel.builder()
                .name("Java").build();

        SkillModel skillGo = SkillModel.builder()
                .name("Go").build();

        List<SkillModel> skillModelList = Arrays.asList(
                skillJava, skillGo
        );

        when(skillRepository.findAll((Pageable) any(Pageable.class)))
                .thenReturn(new PageImpl<>(skillModelList));

        Page<SkillDTO> resultPage = skillService.getAllSkills(Pageable.unpaged());
        assertEquals(skillModelList.size(), resultPage.getTotalElements());
        assertEquals(skillModelList.size(), resultPage.getContent().size());
    }
}
