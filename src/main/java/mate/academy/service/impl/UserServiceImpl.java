package mate.academy.service.impl;

import java.util.Optional;
import mate.academy.dao.UserDao;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.User;
import mate.academy.service.UserService;
import mate.academy.util.HashUti;

@Service
public class UserServiceImpl implements UserService {
    @Inject
    private static UserDao dao;

    @Override
    public User add(User user) {
        byte[] salt = HashUti.getSalt();
        user.setSalt(salt);
        user.setPassword(HashUti.getHashedPass(user.getPassword(), salt));
        return dao.add(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return dao.findByEmail(email);
    }
}
