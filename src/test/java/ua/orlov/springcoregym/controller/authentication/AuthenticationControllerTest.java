package ua.orlov.springcoregym.controller.authentication;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ua.orlov.springcoregym.service.user.UserService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void loginThenSuccess() throws Exception {
        when(userService.isUserNameMatchPassword(any(), any())).thenReturn(true);

        MvcResult result = mockMvc.perform(get("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\", \"password\":\"password\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("You are logged in", responseBody);
    }

    @Test
    void loginThenFailure() throws Exception {
        when(userService.isUserNameMatchPassword(any(), any())).thenReturn(false);

        MvcResult result = mockMvc.perform(get("/session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\", \"password\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("You aren't logged in", responseBody);
    }

    @Test
    void changeLoginThenSuccess() throws Exception {
        when(userService.changeUserPassword(any(), any(), any())).thenReturn(true);

        MvcResult result = mockMvc.perform(put("/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\", \"oldPassword\":\"password\", \"newPassword\":\"password\"}"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("You successfully changed password", responseBody);
    }


    @Test
    void changeLoginThenFailure() throws Exception {
        when(userService.changeUserPassword(any(), any(), any())).thenReturn(false);

        MvcResult result = mockMvc.perform(put("/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"username\", \"oldPassword\":\"password\", \"newPassword\":\"password\"}"))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();

        assertEquals("Password hasn't been changed", responseBody);
    }

}