package xyz.drugalev;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import xyz.drugalev.database.JDBCConnection;
import xyz.drugalev.database.MigrationLoader;
import xyz.drugalev.entity.Training;
import xyz.drugalev.entity.TrainingType;
import xyz.drugalev.entity.User;
import xyz.drugalev.repository.TrainingRepository;
import xyz.drugalev.repository.TrainingTypeRepository;
import xyz.drugalev.repository.UserRepository;
import xyz.drugalev.repository.impl.*;
import xyz.drugalev.in.servlet.TrainingsServlet;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class TrainingServletTest {
    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");
    private static User mockUser = new User();
    private static User adminUser;
    private static TrainingType mockTrainingType;
    private static TrainingRepository trainingRepository;
    @InjectMocks
    private TrainingsServlet trainingsServlet;
    @Mock
    HttpSession session;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @BeforeAll
    static void setUp() throws Exception {
        container.start();
        JDBCConnection.setConnectionCredentials(container.getJdbcUrl(), container.getUsername(), container.getPassword());
        MigrationLoader.migrate();

        UserRepository userRepository = new UserRepositoryImpl(new RoleRepositoryImpl(new PrivilegeRepositoryImpl()));
        trainingRepository = new TrainingRepositoryImpl(new TrainingTypeRepositoryImpl(), new TrainingDataRepositoryImpl(), userRepository);

        mockUser.setUsername("existingTestUser");
        mockUser.setPassword("existingTestPassword");
        userRepository.save(mockUser);
        mockUser = userRepository.find("existingTestUser").orElseThrow();
        adminUser = userRepository.find("admin").orElseThrow();

        TrainingTypeRepository trainingTypeRepository = new TrainingTypeRepositoryImpl();
        trainingTypeRepository.save("testType1");
        mockTrainingType = trainingTypeRepository.find("testType1").orElseThrow();
        trainingTypeRepository.save("testType2");
    }

    @Test
    void TrainingsServlet_shouldSaveTraining_whenValidDataIsProvided() throws Exception {
        String jsonInput = "{" +
                "\"date\": \"%s\",".formatted(LocalDate.now()) +
                "\"trainingType\": {" +
                "\"id\": %d".formatted(mockTrainingType.getId()) +
                "}," +
                "\"trainingData\": [" +
                "{\"name\": \"Lifts\", \"value\": 200}" +
                "]," +
                "\"duration\": 10," +
                "\"burnedCalories\": 10" +
                "}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURI()).thenReturn("/trainings");
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getSession()).thenReturn(session);

        trainingsServlet.init();
        trainingsServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        Assertions.assertTrue(trainingRepository.find(adminUser, mockTrainingType, LocalDate.now()).isPresent());
    }

    @Test
    void TrainingsServlet_shouldNotSaveTraining_whenInvalidDataIsProvided() throws Exception {
        String jsonInput = "{" +
                "\"date\": \"%s\",".formatted(LocalDate.now().plusDays(1)) +
                "\"trainingType\": {" +
                "\"id\": %d".formatted(mockTrainingType.getId()) +
                "}," +
                "\"trainingData\": [" +
                "{\"name\": \"Lifts\", \"value\": 200}" +
                "]," +
                "\"duration\": 10," +
                "\"burnedCalories\": 10" +
                "}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURI()).thenReturn("/trainings");

        trainingsServlet.init();
        trainingsServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void TrainingsServlet_shouldNotSaveTraining_whenExistingDataIsProvided() throws Exception {
        String jsonInput = "{" +
                "\"date\": \"%s\",".formatted(LocalDate.now().minusDays(1)) +
                "\"trainingType\": {" +
                "\"id\": %d".formatted(mockTrainingType.getId()) +
                "}," +
                "\"trainingData\": [" +
                "{\"name\": \"Lifts\", \"value\": 200}" +
                "]," +
                "\"duration\": 10," +
                "\"burnedCalories\": 10" +
                "}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURI()).thenReturn("/trainings");
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getSession()).thenReturn(session);

        trainingsServlet.init();
        trainingsServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_CREATED);
        trainingsServlet.doPost(request, response);
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    void TrainingsServlet_shouldNotSaveTraining_whenInvalidDataFormatIsProvided() throws Exception {
        String jsonInput = "{" +
                "\"NotADate\": \"%s\",".formatted(LocalDate.now()) +
                "\"trainingType\": {" +
                "\"id\": %d".formatted(mockTrainingType.getId()) +
                "}," +
                "\"trainingData\": [" +
                "{\"name\": \"Lifts\", \"value\": 200}" +
                "]," +
                "\"duration\": 10," +
                "\"burnedCalories\": 10" +
                "}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);
        when(request.getRequestURI()).thenReturn("/trainings");

        trainingsServlet.init();
        trainingsServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void TrainingsServlet_shouldDeleteTraining_whenValidDataIsProvided() throws Exception {
        Training t = new Training();
        t.setPerformer(mockUser);
        t.setTrainingType(mockTrainingType);
        t.setDate(LocalDate.now().minusDays(10));
        t.setDuration(10);
        t.setBurnedCalories(10);

        trainingRepository.save(t);

        t = trainingRepository.find(mockUser, mockTrainingType, LocalDate.now().minusDays(10)).orElseThrow();

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/trainings/%d".formatted(t.getId()));
        when(session.getAttribute("user")).thenReturn(t.getPerformer());
        when(request.getSession()).thenReturn(session);

        trainingsServlet.init();
        trainingsServlet.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);

        Assertions.assertFalse(trainingRepository.find(t.getPerformer(), t.getTrainingType(), t.getDate()).isPresent());
    }

    @Test
    void TrainingsServlet_shouldDeleteTraining_whenInvalidDataIsProvided() {
        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/trainings/%d".formatted(9999999));
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getSession()).thenReturn(session);

        trainingsServlet.init();
        trainingsServlet.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void TrainingsServlet_shouldUpdateTraining_whenValidDataIsProvided() throws Exception {
        Training t = new Training();
        t.setPerformer(mockUser);
        t.setTrainingType(mockTrainingType);
        t.setDate(LocalDate.now().minusDays(10));
        t.setDuration(10);
        t.setBurnedCalories(10);
        trainingRepository.save(t);

        String jsonInput = "{" +
                "\"date\": \"%s\",".formatted(t.getDate()) +
                "\"trainingType\": {" +
                "\"id\": %d".formatted(t.getTrainingType().getId()) +
                "}," +
                "\"duration\": 15," +
                "\"burnedCalories\": 15" +
                "}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);

        t = trainingRepository.find(mockUser, mockTrainingType, LocalDate.now().minusDays(10)).orElseThrow();

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/trainings/%d".formatted(t.getId()));
        when(session.getAttribute("user")).thenReturn(t.getPerformer());
        when(request.getSession()).thenReturn(session);

        trainingsServlet.init();
        trainingsServlet.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);

        t = trainingRepository.find(t.getId()).orElseThrow();

        Assertions.assertEquals(15, t.getDuration());
        Assertions.assertEquals(15, t.getBurnedCalories());
    }

    @Test
    void TrainingsServlet_getUserTrainings_whenDataIsProvided() throws Exception {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        Training t = new Training();
        t.setPerformer(mockUser);
        t.setTrainingType(mockTrainingType);
        t.setDate(LocalDate.now().minusDays(100));
        t.setDuration(10);
        t.setBurnedCalories(10);

        trainingRepository.save(t);

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/trainings");
        when(response.getWriter()).thenReturn(writer);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getSession()).thenReturn(session);

        trainingsServlet.init();
        trainingsServlet.doGet(request, response);
        Assertions.assertNotEquals("[]", out.toString());

        Assertions.assertTrue(out.toString().contains(LocalDate.now().minusDays(100).toString()));
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void TrainingsServlet_getUserTrainingsBetweenDates_whenDataIsProvided() throws Exception {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        Training t = new Training();
        t.setPerformer(mockUser);
        t.setTrainingType(mockTrainingType);
        t.setDuration(10);
        t.setBurnedCalories(10);

        t.setDate(LocalDate.now().minusDays(90));
        trainingRepository.save(t);
        t.setDate(LocalDate.now().minusDays(89));
        trainingRepository.save(t);
        t.setDate(LocalDate.now().minusDays(88));
        trainingRepository.save(t);

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/trainings");
        when(request.getParameter("startDate")).thenReturn(LocalDate.now().minusDays(90).toString());
        when(request.getParameter("endDate")).thenReturn(LocalDate.now().minusDays(88).toString());
        when(response.getWriter()).thenReturn(writer);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getSession()).thenReturn(session);

        trainingsServlet.init();
        trainingsServlet.doGet(request, response);
        Assertions.assertNotEquals("[]", out.toString());

        Assertions.assertTrue(out.toString().contains(LocalDate.now().minusDays(90).toString()));
        Assertions.assertTrue(out.toString().contains(LocalDate.now().minusDays(89).toString()));
        Assertions.assertTrue(out.toString().contains(LocalDate.now().minusDays(88).toString()));
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void TrainingsServlet_getUserSpecificTraining_whenDataIsProvided() throws Exception {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        Training t = new Training();
        t.setPerformer(mockUser);
        t.setTrainingType(mockTrainingType);
        t.setDuration(10);
        t.setBurnedCalories(10);
        t.setDate(LocalDate.now().minusDays(70));
        trainingRepository.save(t);

        t = trainingRepository.find(t.getPerformer(), t.getTrainingType(), t.getDate()).orElseThrow();

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/trainings/%d".formatted(t.getId()));
        when(session.getAttribute("user")).thenReturn(t.getPerformer());
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writer);

        trainingsServlet.init();
        trainingsServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    void TrainingsServlet_getAllTraining_whenDataIsProvided() throws Exception {
        StringWriter outAll = new StringWriter();
        PrintWriter writerAll = new PrintWriter(outAll);

        StringWriter outUser = new StringWriter();
        PrintWriter writerUser = new PrintWriter(outUser);

        Training t = new Training();
        t.setPerformer(mockUser);
        t.setTrainingType(mockTrainingType);
        t.setDuration(10);
        t.setBurnedCalories(10);
        t.setDate(LocalDate.now().minusDays(55));
        trainingRepository.save(t);
        t.setDate(LocalDate.now().minusDays(54));
        trainingRepository.save(t);
        t.setPerformer(adminUser);
        trainingRepository.save(t);

        when(request.getContextPath()).thenReturn("");
        when(request.getRequestURI()).thenReturn("/trainings/all");
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getSession()).thenReturn(session);
        when(response.getWriter()).thenReturn(writerAll);

        trainingsServlet.init();
        trainingsServlet.doGet(request, response);

        when(request.getRequestURI()).thenReturn("/trainings");
        when(response.getWriter()).thenReturn(writerUser);

        trainingsServlet.init();
        trainingsServlet.doGet(request, response);


        Assertions.assertTrue(outAll.toString().length() > outUser.toString().length() );
    }
}
