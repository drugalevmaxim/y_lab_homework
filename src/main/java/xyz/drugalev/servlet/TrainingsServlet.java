package xyz.drugalev.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import xyz.drugalev.dto.TrainingDto;
import xyz.drugalev.entity.User;
import xyz.drugalev.exception.TrainingAlreadyExistsException;
import xyz.drugalev.exception.TrainingNotFoundException;
import xyz.drugalev.repository.impl.TrainingDataRepositoryImpl;
import xyz.drugalev.repository.impl.TrainingRepositoryImpl;
import xyz.drugalev.repository.impl.TrainingTypeRepositoryImpl;
import xyz.drugalev.repository.impl.UserRepositoryImpl;
import xyz.drugalev.service.TrainingService;
import xyz.drugalev.service.impl.TrainingServiceImpl;
import xyz.drugalev.validator.TrainingDtoValidator;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet("/trainings/*")
public class TrainingsServlet extends HttpServlet {
    private TrainingService trainingService;
    private ObjectMapper objectMapper;

    @Override
    public void init() {
        this.trainingService = new TrainingServiceImpl(new TrainingRepositoryImpl(
                new TrainingTypeRepositoryImpl(),
                new TrainingDataRepositoryImpl(),
                new UserRepositoryImpl()));
        this.objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        final String regex = "^/trainings/([0-9]+)/?$";
        final Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(req.getRequestURI());
        if (matcher.matches()) {
            long id = Long.parseLong(matcher.group(1));
            handleGetSpecific(req, resp, id);
            return;
        }
        if (!req.getRequestURI().equals("/trainings")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (req.getParameter("startDate") != null && req.getParameter("endDate") != null) {
            handleGetBetween(req, resp);
            return;
        }

        handleGetAll(req, resp);
    }
    private void handleGetSpecific(HttpServletRequest req, HttpServletResponse resp, long id) {
        try {
            User user = (User) req.getSession().getAttribute("user");
            TrainingDto training = trainingService.find(user, id);
            resp.getWriter().println(objectMapper.writeValueAsString(training));
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (TrainingNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void handleGetAll(HttpServletRequest req, HttpServletResponse resp) {
        try {
            List<TrainingDto> trainings = trainingService.findAllByUser((User) req.getSession().getAttribute("user"));
            resp.getWriter().println(objectMapper.writeValueAsString(trainings));
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void handleGetBetween(HttpServletRequest req, HttpServletResponse resp) {
        try {
            LocalDate start = LocalDate.parse(req.getParameter("startDate"));
            LocalDate end = LocalDate.parse(req.getParameter("endDate"));
            List<TrainingDto> trainings = trainingService.findByUserBetweenDates((User) req.getSession().getAttribute("user"), start, end);
            resp.getWriter().println(objectMapper.writeValueAsString(trainings));
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (DateTimeException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        if (!Objects.equals(req.getRequestURI(), "/trainings")) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            TrainingDto training = objectMapper.readValue(req.getReader(), TrainingDto.class);

            if (!TrainingDtoValidator.isValid(training)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            User user = (User) req.getSession().getAttribute("user");
            trainingService.save(user, training);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (TrainingAlreadyExistsException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final String regex = "^/trainings/([0-9]+)/?$";
            final Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(req.getRequestURI());
            if (matcher.matches()) {
                long id = Long.parseLong(matcher.group(1));
                User user = (User) req.getSession().getAttribute("user");
                TrainingDto training = trainingService.find(user, id);
                trainingService.delete(user, training);
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (TrainingNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final String regex = "^/trainings/([0-9]+)/?$";
            final Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(req.getRequestURI());
            if (matcher.matches()) {
                long id = Long.parseLong(matcher.group(1));
                TrainingDto training = objectMapper.readValue(req.getReader(), TrainingDto.class);
                training.setId(id);

                if (!TrainingDtoValidator.isValid(training)) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    return;
                }

                User user = (User) req.getSession().getAttribute("user");
                trainingService.update(user, training);
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (TrainingNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
