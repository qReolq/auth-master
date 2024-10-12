package qreol.project.userservice.service;

import qreol.project.userservice.model.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User getById(Long id);

    User create(User user);

    User update(User user, Long id);

    void delete(Long id);

}
