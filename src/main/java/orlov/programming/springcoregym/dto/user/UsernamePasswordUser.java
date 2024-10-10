package orlov.programming.springcoregym.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsernamePasswordUser {
    private String username;
    private String password;
}
