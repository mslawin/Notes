package pl.mslawin.notes.ws;

import static pl.mslawin.notes.constants.NotesConstants.USER_PARAM;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import pl.mslawin.notes.annotation.RequiresAuthentication;
import pl.mslawin.notes.dto.model.ListDto;
import pl.mslawin.notes.dto.model.TaskListDto;
import pl.mslawin.notes.dto.model.UsersListDto;
import pl.mslawin.notes.dto.request.LoginRequest;
import pl.mslawin.notes.service.TaskService;
import pl.mslawin.notes.service.UserService;

@RestController
@RequestMapping("/user")
public class UserResource {

    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;
    private final TaskService taskService;

    @Inject
    public UserResource(UserService userService, TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<String> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest) throws GeneralSecurityException, IOException {
        String login = userService.handleGoogleLogin(loginRequest.getLoginToken());
        if (login != null) {
            request.getSession().setAttribute(USER_PARAM, login);
            logger.info("Authenticated user: {}", login);
            return ResponseEntity.ok(login);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/connections")
    @RequiresAuthentication
    public ResponseEntity<UsersListDto> getAllConnectedUsers(HttpServletRequest request) {
        String user = (String) request.getSession().getAttribute(USER_PARAM);
        ListDto allListsForUser = taskService.getAllListsForUser(user);
        Set<String> connections = getConnections(allListsForUser);
        connections.remove(user);
        return ResponseEntity.ok(new UsersListDto(connections));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/logout")
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        logger.info("User {} logged out", session.getAttribute(USER_PARAM));
        session.invalidate();
    }

    private Set<String> getConnections(ListDto allListsForUser) {
        Set<String> connections = new HashSet<>();
        for (TaskListDto taskListDto : allListsForUser.getTaskListDtoList()) {
            connections.addAll(taskListDto.getSharedWith());
            connections.add(taskListDto.getOwner());
        }
        return connections;
    }
}