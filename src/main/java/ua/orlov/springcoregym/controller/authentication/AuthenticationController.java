package ua.orlov.springcoregym.controller.authentication;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.orlov.springcoregym.dto.user.ChangeLoginDto;
import ua.orlov.springcoregym.dto.user.UsernamePasswordUser;
import ua.orlov.springcoregym.service.user.UserService;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @GetMapping("/session")
    public ResponseEntity<String> login(@RequestBody @Validated UsernamePasswordUser userNamePasswordUser) {
        if(userService.isUserNameMatchPassword(userNamePasswordUser.getUsername(), userNamePasswordUser.getPassword())){
            return ResponseEntity.ok("You are logged in");
        }

        return ResponseEntity.badRequest().body("You aren't logged in");
    }

    @PutMapping("/password")
    public ResponseEntity<String> changeLogin(@RequestBody @Validated ChangeLoginDto changeLoginDto) {
        if(userService.changeUserPassword(changeLoginDto.getUsername(), changeLoginDto.getOldPassword(),
                changeLoginDto.getNewPassword())) {
            return ResponseEntity.ok("You successfully changed password");
        }

        return ResponseEntity.badRequest().body("Password wasn't changed");
    }
}
