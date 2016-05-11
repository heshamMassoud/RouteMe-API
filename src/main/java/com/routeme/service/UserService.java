package services;

import java.util.List;

import dtos.UserDTO;

public interface UserService {

    UserDTO create(UserDTO user);

    UserDTO delete(String id);

    List<UserDTO> findAll();

    UserDTO findById(String id);

    UserDTO update(UserDTO user);

}
