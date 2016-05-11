package com.routeme.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.routeme.dtl.UserDTO;
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
    public UserDTO create(UserDTO user) {
        User persistedUser = User.getFactory().name(user.getName()).email(user.getEmail()).password(user.getPassword())
                .build();
        persistedUser = repository.save(persistedUser);
        return convertToDTO(persistedUser);
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
    public UserDTO update(UserDTO userModel) {
        User updatedUser = findUserById(userModel.getId());
        updatedUser.update(userModel.getName(), userModel.getEmail(), userModel.getPassword());
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
        dto.setName(model.getName());
        dto.setEmail(model.getEmail());
        dto.setPassword(model.getPassword());
        return dto;
    }

}
