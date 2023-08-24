package com.RDS.skilltree.User;

import com.RDS.skilltree.Skill.SkillModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/")
    public String createUser(@RequestBody UserDRO user) {
        try {
            userService.createUser(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "Success";
    }

    @PutMapping("/")
    public UserModel updateUser(UserModel user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}")
    public String addSkill(@RequestParam UUID skillId, @PathVariable UUID id) {
        return userService.addSkill(skillId, id);
    }

    @GetMapping("/{id}")
    public UserModel getUserById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @GetMapping("/")
    public List<UserDTO> getAllUsers() {
        List<UserDTO> res = userService.getAllUsers();
        return res;
    }
}
