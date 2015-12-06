package pl.mslawin.notes.ws;

import static pl.mslawin.notes.constants.NotesConstants.USER_PARAM;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.mslawin.notes.dto.request.LoginRequest;
import pl.mslawin.notes.service.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {

    private final UserService userService;

    @Inject
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<String> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest) throws GeneralSecurityException, IOException {
        String login = userService.handleGoogleLogin(loginRequest.getLoginToken());
        if (login != null) {
            request.getSession().setAttribute(USER_PARAM, login);
            return ResponseEntity.ok(login);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
    }
}