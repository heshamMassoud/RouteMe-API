package com.routeme.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.routeme.dto.UserDTO;
import com.routeme.model.User;
import com.routeme.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserDTO> create(UserDTO userEntry) {
        if (userExists(userEntry.getEmail())) {
            return Optional.empty();
        }
        User persistedUser = User.getFactory().name(userEntry.getUsername()).email(userEntry.getEmail())
                .password(userEntry.getPassword()).confirmationPassword(userEntry.getConfirmationPassword()).build();
        persistedUser = repository.save(persistedUser);
        return Optional.of(convertToDTO(persistedUser));
    }

    @Override
    public UserDTO delete(String id) {
        User deletedUser = findUserById(id);
        repository.delete(deletedUser);
        return convertToDTO(deletedUser);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> userEntries = repository.findAll();
        return convertToDTOs(userEntries);
    }

    private List<UserDTO> convertToDTOs(List<User> userModels) {
        return userModels.stream().map(this::convertToDTO).collect(toList());
    }

    @Override
    public UserDTO findById(String id) {
        User foundUser = findUserById(id);
        return convertToDTO(foundUser);
    }

    @Override
    public UserDTO update(UserDTO userEntry) {
        User updatedUser = findUserById(userEntry.getId());
        updatedUser.update(userEntry.getUsername(), userEntry.getEmail(), userEntry.getPassword(),
                userEntry.getConfirmationPassword());
        updatedUser = repository.save(updatedUser);
        return convertToDTO(updatedUser);
    }

    private User findUserById(String id) {
        Optional<User> result = repository.findOne(id);
        return result.get();
    }

    private UserDTO convertToDTO(User model) {
        UserDTO dto = new UserDTO();
        dto.setId(model.getId());
        dto.setUsername(model.getUsername());
        dto.setEmail(model.getEmail());
        dto.setPassword(model.getPassword());
        dto.setConfirmationPassword(model.getConfirmationPassword());
        return dto;
    }

    private boolean userExists(String email) {
        List<User> result = repository.findByEmail(email);
        return !result.isEmpty();
    }

    @Override
    public Optional<UserDTO> findByCredentials(String email, String password) {
        List<User> result = repository.findByEmailAndPassword(email, password);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        UserDTO userDto = convertToDTO(result.get(0));
        return Optional.of(userDto);
    }

}
