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
import xyz.drugalev.in.servlet.LoginServlet;

import java.io.BufferedReader;
import java.io.StringReader;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class LoginServletTest {
    @Container
    private static final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.2-alpine3.19");
    private static final User mockUser = new User();
    @InjectMocks
    private LoginServlet loginServlet;
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
        mockUser.setUsername("existingTestUser");
        mockUser.setPassword("existingTestPassword");
        userRepository.save(mockUser);
    }

    @Test
    @DisplayName("Login user with valid credentials")
    void LoginServlet_shouldLoginUser_whenValidCredentialsAreProvided() throws Exception {
        String jsonInput = "{\"username\":\"%s\", \"password\":\"%s\"}".formatted(mockUser.getUsername(), mockUser.getPassword());
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));

        when(request.getReader()).thenReturn(reader);
        when(request.getSession()).thenReturn(session);

        loginServlet.init();
        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
    }

    @Test
    @DisplayName("Login user with valid credentials (User does not exist)")
    void LoginServlet_shouldNotLoginUser_whenNonExistingUserCredentialsAreProvided() throws Exception {
        String jsonInput = "{\"username\":\"%s\", \"password\":\"%s\"}".formatted("nonExistingTestUser","nonExistingTestUser");
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        when(request.getSession()).thenReturn(session);

        loginServlet.init();
        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Login user with invalid credentials (invalid JSON format)")
    void LoginServlet_shouldNotLoginUser_whenInvalidUserCredentialsAreProvided() throws Exception {
        String jsonInput = "{\"NotAUsername\":\"%s\", \"NotAPassword\":\"%s\"}".formatted(mockUser.getUsername(), mockUser.getPassword());
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        loginServlet.init();
        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Login user when database is down")
    void  LoginServlet_shouldNotLoginUser_whenDatabaseDown() throws Exception {
        String jsonInput = "{\"username\":\"%s\", \"password\":\"%s\"}".formatted(mockUser.getUsername(), mockUser.getPassword());
        BufferedReader reader = new BufferedReader(new StringReader(jsonInput));
        when(request.getReader()).thenReturn(reader);

        loginServlet.init();
        container.stop();
        loginServlet.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        container.start();
    }
}
