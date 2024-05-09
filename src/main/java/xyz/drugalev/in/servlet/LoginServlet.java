package xyz.drugalev.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import xyz.drugalev.database.MigrationLoader;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.UserNotFoundException;
import xyz.drugalev.repository.impl.PrivilegeRepositoryImpl;
import xyz.drugalev.repository.impl.RoleRepositoryImpl;
import xyz.drugalev.repository.impl.UserRepositoryImpl;
import xyz.drugalev.service.AuthService;
import xyz.drugalev.service.impl.AuthServiceImpl;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private AuthService authService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        MigrationLoader.migrate();
        authService = new AuthServiceImpl(new UserRepositoryImpl(
                new RoleRepositoryImpl(
                        new PrivilegeRepositoryImpl()
                )));
        this.objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = objectMapper.readValue(req.getReader(), User.class);
            req.getSession().setAttribute("user", authService.login(user.getUsername(), user.getPassword()));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (UserNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
