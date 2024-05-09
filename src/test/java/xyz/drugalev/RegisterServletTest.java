package xyz.drugalev;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
import xyz.drugalev.repository.UserRepository;
import xyz.drugalev.repository.impl.PrivilegeRepositoryImpl;
import xyz.drugalev.repository.impl.RoleRepositoryImpl;
import xyz.drugalev.repository.impl.UserRepositoryImpl;
import xyz.drugalev.in.servlet.RegisterServlet;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class RegisterServletTest {
    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");
    private static final User mockUser = new User();
    @Mock
    HttpSession session;
    @InjectMocks
    private RegisterServlet registerServlet;
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
    }

    @Test
    @DisplayName("Register user with valid credentials")
    void RegisterServlet_shouldCreateNewUser_whenValidCredentialsAreProvided() throws Exception {
        String jsonInput = "{\"username\":\"testUser\", \"password\":\"testPassword\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(request.getSession()).thenReturn(session);

        registerServlet.init();
        registerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    @DisplayName("Register user with invalid credentials (existing user)")
    void RegisterServlet_shouldNotCreateNewUser_whenExistingUsersCredentialsAreProvided() throws Exception {
        String jsonInput = "{\"username\":\"%s\", \"password\":\"%s\"}".formatted(mockUser.getUsername(), mockUser.getPassword());
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(request.getSession()).thenReturn(session);

        registerServlet.init();
        registerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Register user with invalid credentials (invalid JSON format)")
    void RegisterServlet_shouldNotCreateNewUser_whenInvalidJsonFormatCredentialsAreProvided() throws Exception {
        String invalidJsonInput = "{\"notAUsername\":\"existingTestUser\", \"notAPassword\":\"existingTestPassword\"}";
        BufferedReader reader = new BufferedReader(new StringReader(invalidJsonInput));
        when(request.getReader()).thenReturn(reader);

        registerServlet.init();
        registerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Register user with invalid credentials (Validation error)")
    void RegisterServlet_shouldNotCreateNewUser_whenInvalidUserCredentialsAreProvided() throws Exception {
        String jsonInput = "{\"username\":\"<6\", \"password\":\"<6\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        registerServlet.init();
        registerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Register user when database is down")
    void RegisterServlet_shouldNotCreateNewUser_whenDatabaseDown() throws Exception {
        String jsonInput = "{\"username\":\"testUser2\", \"password\":\"testUser2\"}";
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        registerServlet.init();
        container.stop();
        registerServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        container.start();
    }
}
