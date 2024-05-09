package xyz.drugalev;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
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
import xyz.drugalev.entity.User;
import xyz.drugalev.in.servlet.TrainingTypeServlet;
import xyz.drugalev.repository.TrainingTypeRepository;
import xyz.drugalev.repository.UserRepository;
import xyz.drugalev.repository.impl.PrivilegeRepositoryImpl;
import xyz.drugalev.repository.impl.RoleRepositoryImpl;
import xyz.drugalev.repository.impl.TrainingTypeRepositoryImpl;
import xyz.drugalev.repository.impl.UserRepositoryImpl;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class TrainingTypeServletTest {
    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");
    private static final User mockUser = new User();
    private static User adminUser;
    @Mock
    HttpSession session;
    @InjectMocks
    private TrainingTypeServlet trainingTypeServlet;
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
        mockUser.setUsername("existingTestUser");
        mockUser.setPassword("existingTestPassword");
        userRepository.save(mockUser);
        adminUser = userRepository.find("admin").orElseThrow();

        TrainingTypeRepository trainingTypeRepository = new TrainingTypeRepositoryImpl();
        trainingTypeRepository.save("testType");
    }

    @Test
    @DisplayName("Saving new training type with valid data provided")
    void TrainingTypeServlet_shouldSaveNewTrainingType_whenValidUserAndValidTrainingTypeProvided() throws Exception {
        String jsonInput = "{\"name\":\"testType1\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getSession()).thenReturn(session);

        trainingTypeServlet.init();
        trainingTypeServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Not saving new training type with invalid data provided (user that has no privileges)")
    void TrainingTypeServlet_shouldNotSaveNewTrainingType_whenInvalidUserAndValidTrainingTypeProvided() throws Exception {
        String jsonInput = "{\"name\":\"testType2\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);
        when(session.getAttribute("user")).thenReturn(mockUser);
        when(request.getSession()).thenReturn(session);

        trainingTypeServlet.init();
        trainingTypeServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    @Test
    @DisplayName("Not saving new training type with invalid data provided (existing training type)")
    void TrainingTypeServlet_shouldNotSaveNewTrainingType_whenValidUserAndExistingTrainingTypeProvided() throws Exception {
        String jsonInput = "{\"name\":\"testType\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);
        when(session.getAttribute("user")).thenReturn(adminUser);
        when(request.getSession()).thenReturn(session);

        trainingTypeServlet.init();
        trainingTypeServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Not saving new training type with invalid data provided (invalid JSON format)")
    void TrainingTypeServlet_shouldNotSaveNewTrainingType_whenValidUserAndInvalidTrainingTypeProvided() throws Exception {
        String jsonInput = "{\"notAName\":\"testType3\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);

        trainingTypeServlet.init();
        trainingTypeServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Getting all training types")
    void TrainingTypeServlet_shouldReturnTrainingType() throws Exception {
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);

        when(response.getWriter()).thenReturn(writer);
        trainingTypeServlet.init();
        trainingTypeServlet.doGet(request, response);
        Assertions.assertNotEquals("[]", out.toString());

        Assertions.assertTrue(out.toString().contains("testType"));
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Not getting all training types (database is down)")
    void TrainingTypeServlet_shouldNotReturnTrainingType_whenDatabaseIsDown() {
        container.stop();
        trainingTypeServlet.init();
        trainingTypeServlet.doGet(request, response);
        container.start();
        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
