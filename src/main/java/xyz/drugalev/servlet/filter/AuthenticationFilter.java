package xyz.drugalev.servlet.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;


@WebFilter("/*")
public class AuthenticationFilter extends HttpFilter {

    private static final String LOGIN_URI = "/login";
    private static final String REGISTER_URI = "/register";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String requestURI = req.getRequestURI().substring(req.getContextPath().length());


        HttpSession session = req.getSession(false);

        boolean isLoggedIn = session != null && session.getAttribute("user") != null;

        if (isLoggedIn && (requestURI.equals(LOGIN_URI) || requestURI.equals(REGISTER_URI))) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        } else if (!isLoggedIn && !(requestURI.equals(LOGIN_URI) || requestURI.equals(REGISTER_URI))) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            chain.doFilter(request, response);
        }
    }
}
