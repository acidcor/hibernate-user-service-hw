package mate.academy.service.impl;

import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.AuthenticationService;
import mate.academy.service.UserService;
import mate.academy.util.HashUti;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String email, String password) throws AuthenticationException {
        User user = userService.findByEmail(email).orElseThrow(
                () -> new AuthenticationException("User with such email isn't exists: " + email)
        );
        String cryptedPass = HashUti.getHashedPass(password, user.getSalt());
        if (cryptedPass.equals(user.getPassword())) {
            return user;
        }
        throw new AuthenticationException("Password doesn't match!");
    }

    @Override
    public User register(String email, String password) throws RegistrationException {
        checkRegistrationData(email, password);
        User user = new User();
        user.setPassword(password);
        user.setEmail(email);
        return userService.add(user);
    }

    private void checkRegistrationData(String email, String password) throws RegistrationException {
        checkEmail(email);
        checkPass(password);
    }

    private void checkPass(String password) throws RegistrationException {
        if (password.isEmpty()) {
            throw new RegistrationException("Password field is empty");
        }
    }

    private void checkEmail(String email) throws RegistrationException {
        if (email.isEmpty()) {
            throw new RegistrationException("Email field is empty");
        }
        if (!userService.findByEmail(email).isEmpty()) {
            throw new RegistrationException("User with such email already exists: " + email);
        }
    }
}
