package xyz.drugalev.in.servlet;

import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import xyz.drugalev.database.MigrationLoader;
import xyz.drugalev.exception.InvalidParametersException;
import xyz.drugalev.exception.UserAlreadyExistsException;
import xyz.drugalev.repository.impl.PrivilegeRepositoryImpl;
import xyz.drugalev.repository.impl.RoleRepositoryImpl;
import xyz.drugalev.repository.impl.UserRepositoryImpl;
import xyz.drugalev.entity.User;
import xyz.drugalev.service.AuthService;
import xyz.drugalev.service.impl.AuthServiceImpl;

import java.io.IOException;
import java.sql.SQLException;


@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

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
            req.getSession().setAttribute("user", authService.register(user.getUsername(), user.getPassword()));
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (DatabindException | UserAlreadyExistsException | InvalidParametersException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
