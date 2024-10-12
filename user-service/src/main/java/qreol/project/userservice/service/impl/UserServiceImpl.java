package qreol.project.userservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qreol.project.userservice.model.User;
import qreol.project.userservice.model.exception.ResourceNotFoundException;
import qreol.project.userservice.repository.UserRepository;
import qreol.project.userservice.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User getById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with id %s is not found", id))
                );
    }

    @Override
    @Transactional
    public User create(User user) {
        enrichUser(user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User update(User user, Long id) {
        User toUpdate = getById(id);

        if (user.getUsername() != null && !user.getUsername().equals(toUpdate.getUsername())) {
            toUpdate.setUsername(user.getUsername());
        }
        if (user.getEmail() != null && !user.getEmail().equals(toUpdate.getEmail())) {
            toUpdate.setEmail(user.getEmail());
        }
        if (user.getPassword() != null && !user.getPassword().equals(toUpdate.getPassword())) {
            toUpdate.setPassword(user.getPassword());
        }

        toUpdate.setUpdatedAt(LocalDateTime.now());

        return userRepository.save(toUpdate);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User toDelete = getById(id);
        userRepository.delete(toDelete);
    }

    private void enrichUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
    }

}
