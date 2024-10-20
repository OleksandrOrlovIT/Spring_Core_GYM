package ua.orlov.springcoregym.controller.authentication;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.Map;

@RestController
@AllArgsConstructor
public class AuthenticationController {

    private final UserService userService;

    @Operation(summary = "User login", description = "Authenticate a user by username and password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful login",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed or bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found (e.g., NoSuchElementException)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @GetMapping("/session")
    public ResponseEntity<String> login(@RequestBody @Validated UsernamePasswordUser userNamePasswordUser) {
        if(userService.isUserNameMatchPassword(userNamePasswordUser.getUsername(), userNamePasswordUser.getPassword())){
            return ResponseEntity.ok("You are logged in");
        }

        return ResponseEntity.badRequest().body("You aren't logged in");
    }

    @Operation(summary = "Change user password", description = "Change the password for an existing user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Validation failed or bad request",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Resource not found (e.g., NoSuchElementException)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class)))
    })
    @PutMapping("/password")
    public ResponseEntity<String> changeLogin(@RequestBody @Validated ChangeLoginDto changeLoginDto) {
        if(userService.changeUserPassword(changeLoginDto.getUsername(), changeLoginDto.getOldPassword(),
                changeLoginDto.getNewPassword())) {
            return ResponseEntity.ok("You successfully changed password");
        }

        return ResponseEntity.badRequest().body("Password hasn't been changed");
    }
}
