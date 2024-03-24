package com.RDS.skilltree.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.RDS.skilltree.Skill.SkillDTO;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.Skill.SkillRepository;
import com.RDS.skilltree.Skill.SkillsServiceImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class SkillsServiceTest {

    @Mock private SkillRepository skillRepository;

    @InjectMocks @Autowired private SkillsServiceImpl skillService;

    @Test
    public void testGetSkillById() {
        UUID skillId = UUID.randomUUID();
        SkillModel skillModel = SkillModel.builder().id(skillId).build();

        when(skillRepository.findById(skillId)).thenReturn(Optional.of(skillModel));

        SkillDTO result = skillService.getSkillById(skillId);
        assertNotNull(result);
        assertEquals(skillId, result.getId(), "The skill Id doesn't matches the expected skillId");
    }

    @Test
    public void testGetSkillsByName() {
        String skillName = "Java";
        SkillModel skillModel = SkillModel.builder().name("Java").build();

        when(skillRepository.findByName(skillName)).thenReturn(Optional.of(skillModel));

        SkillDTO result = skillService.getSkillByName("Java");
        assertNotNull(result);
        assertEquals(
                skillName, result.getName(), "The skill name doesn't match the expected skill name");
    }

    @Test
    public void testGetAllSkills() {
        SkillModel skillJava = SkillModel.builder().name("Java").build();

        SkillModel skillGo = SkillModel.builder().name("Go").build();

        List<SkillModel> skillModelList = Arrays.asList(skillJava, skillGo);

        when(skillRepository.findAll((Pageable) any(Pageable.class)))
                .thenReturn(new PageImpl<>(skillModelList));

        Pageable pageable = PageRequest.of(2, 1);
        Page<SkillDTO> resultPage = skillService.getAllSkills(pageable);
        assertNotNull(resultPage);
        assertEquals(
                skillModelList.size(),
                resultPage.getTotalElements(),
                "The number of elements returned is not equal to the expected size");
        assertEquals(
                skillModelList.size(),
                resultPage.getContent().size(),
                "The content returned is not equal to the expected content");
        assertEquals(
                "Java",
                resultPage.getContent().get(0).getName(),
                "The returned skill on page 0 doesn't match the actual skill");
        assertEquals(
                "Go",
                resultPage.getContent().get(1).getName(),
                "The returned skill on page 0 doesn't match the actual skill");
    }
}
