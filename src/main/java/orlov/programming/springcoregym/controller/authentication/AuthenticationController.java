package orlov.programming.springcoregym.controller.authentication;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import orlov.programming.springcoregym.dto.user.ChangeLoginDto;
import orlov.programming.springcoregym.dto.user.UsernamePasswordUser;
import orlov.programming.springcoregym.service.authentication.AuthenticationService;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public ResponseEntity<String> registerTrainee(@RequestBody @Validated UsernamePasswordUser userNamePasswordUser) {
        if(authenticationService.authenticateUser(userNamePasswordUser.getUsername(), userNamePasswordUser.getPassword())){
            return ResponseEntity.ok("You are logged in");
        }

        return ResponseEntity.badRequest().body("You aren't logged in");
    }

    @PutMapping("/change-login")
    public ResponseEntity<String> changeLogin(@RequestBody @Validated ChangeLoginDto changeLoginDto) {
        authenticationService.changeLogin(changeLoginDto.getUsername(), changeLoginDto.getOldPassword(),
                changeLoginDto.getNewPassword());

        return ResponseEntity.ok("You successfully changed password");
    }
}
