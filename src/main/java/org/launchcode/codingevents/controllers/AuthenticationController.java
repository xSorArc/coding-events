package org.launchcode.codingevents.controllers;

import org.launchcode.codingevents.data.UserRepository;
import org.launchcode.codingevents.models.User;
import org.launchcode.codingevents.models.dto.LoginFormDTO;
import org.launchcode.codingevents.models.dto.RegisterFormDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;

    private static final String userSessionKey = "user";   // Key used to store user IDs.

    public User getUserFromSession(HttpSession session) {
        // Looks for data with the key "user" in the user's session.
        Integer userId = (Integer) session.getAttribute(userSessionKey);
        if (userId == null) {
            return null;
        }
        // If it finds one, it attempts to retrieve the User from the database.
        Optional<User> user = userRepository.findById(userId);
        // If no User ID is in the session, or if there is no user with the given ID, null is returned.
        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    // Uses an HttpSession object to store k/v pair.
    private static void setUserInSession(HttpSession session, User user) {
        session.setAttribute(userSessionKey, user.getId());
    }

    @GetMapping("/register")
    public String displayRegistrationForm(Model model) {
        model.addAttribute(new RegisterFormDTO());
        model.addAttribute("title", "Register");
        return "register";
    }

    // Defines the handler method at the route /register that takes a valid RegisterFormDTO object, associated errors,
    // and a Model. In addition, the method needs an HttpServletRequest object. This object represents the incoming
    // request, and will be provided by Spring.
    @PostMapping("/register")
    public String processRegistrationForm(@ModelAttribute @Valid RegisterFormDTO registerFormDTO,
                                          Errors errors, HttpServletRequest request,
                                          Model model) {

        if (errors.hasErrors()) {  // Return the user to the form if any validation errors occur.
            model.addAttribute("title", "Register");
            return "register";
        }

        // Retrieve the user with the given username from the database.
        User existingUser = userRepository.findByUsername(registerFormDTO.getUsername());

        // If a user with the given username already exists, register a custom error with the errors object and
        // return the user to the form.
        if (existingUser != null) {
            // 1) Field that contains the error 2) Label representing the error. This allows error messages to be
            // imported from another file. While we don’t have such a file, this parameter is required.
            // 3) A default message to use if no external error message file is available (as is the case here).
            errors.rejectValue("username", "username.alreadyexists", "A user with that username already exists");
            model.addAttribute("title", "Register");
            return "register";
        }

        // Compare the two passwords submitted. If they do not match, register a custom error & return user to form.
        String password = registerFormDTO.getPassword();
        String verifyPassword = registerFormDTO.getVerifyPassword();
        if (!password.equals(verifyPassword)) {
            errors.rejectValue("password", "passwords.mismatch", "Passwords do not match");
            model.addAttribute("title", "Register");
            return "register";
        }

        // At this point, we know that a user with the given username does NOT already exist, and the rest of the form
        // data is valid. So we create a new user object, store it in the database, and then create a new session.
        User newUser = new User(registerFormDTO.getUsername(), registerFormDTO.getPassword());
        userRepository.save(newUser);
        setUserInSession(request.getSession(), newUser);

        return "redirect:";  // Redirects user to home page.
    }

    @GetMapping("/login")
    public String displayLoginForm(Model model) {
        model.addAttribute(new LoginFormDTO());
        model.addAttribute("title", "Log In");
        return "login";
    }

    @PostMapping("/login")
    public String processLoginForm(@ModelAttribute @Valid LoginFormDTO loginFormDTO,
                                   Errors errors, HttpServletRequest request,
                                   Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("title", "Log In");
            return "login";
        }
        //  Retrieves the User object with the given password from the database.
        User theUser = userRepository.findByUsername(loginFormDTO.getUsername());

        // If no such user exists, register a custom error and return to the form.
        if (theUser == null) {
            errors.rejectValue("username", "user.invalid", "The given username does not exist");
            model.addAttribute("title", "Log In");
            return "login";
        }

        String password = loginFormDTO.getPassword(); // Retrieves the submitted password from the form DTO.

        // Password verification uses the User.isMatchingPassword() method, which handles the details associated with
        // checking hashed passwords.
        if (!theUser.isMatchingPassword(password)) {
            errors.rejectValue("password", "password.invalid", "Invalid password");
            model.addAttribute("title", "Log In");
            return "login";
        }
        // At this point, we know the given user exists and that the submitted password is correct.
        // So we create a new session for the user.
        setUserInSession(request.getSession(), theUser);

        return "redirect:";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/login";
    }
}
