package xyz.drugalev.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import xyz.drugalev.dto.AuditDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.AccessDeniedException;
import xyz.drugalev.repository.impl.AuditRepositoryImpl;
import xyz.drugalev.repository.impl.PrivilegeRepositoryImpl;
import xyz.drugalev.repository.impl.RoleRepositoryImpl;
import xyz.drugalev.repository.impl.UserRepositoryImpl;
import xyz.drugalev.service.AuditService;
import xyz.drugalev.service.impl.AuditServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/audit")
public class AuditServlet extends HttpServlet {
    private AuditService auditService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        auditService = new AuditServiceImpl(new AuditRepositoryImpl(new UserRepositoryImpl(
                new RoleRepositoryImpl(
                        new PrivilegeRepositoryImpl()
                ))));
        this.objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            User user = (User) req.getSession().getAttribute("user");
            List<AuditDto> trainings = auditService.findAll(user);
            resp.getWriter().println(objectMapper.writeValueAsString(trainings));
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
