package xyz.drugalev.in.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import xyz.drugalev.dto.TrainingTypeDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.AccessDeniedException;
import xyz.drugalev.repository.impl.TrainingTypeRepositoryImpl;
import xyz.drugalev.service.TrainingTypeService;
import xyz.drugalev.service.impl.TrainingTypeServiceImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/training_types")
public class TrainingTypeServlet extends HttpServlet {

    TrainingTypeService trainingTypeService;
    ObjectMapper objectMapper;

    @Override
    public void init() {
        trainingTypeService = new TrainingTypeServiceImpl(new TrainingTypeRepositoryImpl());
        this.objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<TrainingTypeDto> trainings = trainingTypeService.findAll();
            resp.getWriter().println(objectMapper.writeValueAsString(trainings));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            TrainingTypeDto trainingTypeDto =  objectMapper.readValue(req.getReader(), TrainingTypeDto.class);
            User user = (User) req.getSession().getAttribute("user");
            trainingTypeService.save(user, trainingTypeDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (AccessDeniedException e) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
