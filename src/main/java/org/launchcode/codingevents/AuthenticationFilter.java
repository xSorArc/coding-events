package org.launchcode.codingevents;

import org.launchcode.codingevents.controllers.AuthenticationController;
import org.launchcode.codingevents.data.UserRepository;
import org.launchcode.codingevents.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AuthenticationFilter extends HandlerInterceptorAdapter implements HandlerInterceptor {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationController authenticationController;

    private static final List<String> whitelist = Arrays.asList("/login", "/register", "/logout", "/css");

    // This method takes a string representing a URL path and checks to see if it starts with any of the entries in
    // whitelist. If you wanted to be more restrictive, you could use .equals() instead of .startsWith().
    // If the path is whitelisted, we return true. Otherwise, we return false.
    private static boolean isWhitelisted(String path) {
        for (String pathRoot : whitelist) {
            if (path.startsWith(pathRoot)) {
                return true;
            }
        }
        return false;
    }

    // This method has the effect of preventing access to every page on the app if a user is not logged in.
    // This creates one not-so-minor problem: How will a user access the login page if they are not logged in?
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {

        if (isWhitelisted(request.getRequestURI())) { // Don't require sign-in for whitelisted pages
            return true; // Checks the path against the whitelist, returning true if the path is whitelisted.
        }
        // Retrieves the user’s session object, which is contained in the request.
        HttpSession session = request.getSession();
        // Retrieves the User object corresponding to the given user. This will be null if the user is not logged in.
        User user = authenticationController.getUserFromSession(session);

        // The user object is non-null, so the user is logged in. Allow the request to be handled as normal.
        if (user != null) {
            return true; // User is logged in.
        }

        response.sendRedirect("/login"); // The user object is null, so we redirect the user to the login page.
        return false; // User is not logged in.
    }
}
