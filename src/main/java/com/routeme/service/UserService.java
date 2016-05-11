package com.routeme.service;

import java.util.List;

import com.routeme.dtl.UserDTO;

public interface UserService {

    UserDTO create(UserDTO user);

    UserDTO delete(String id);

    List<UserDTO> findAll();

    UserDTO findById(String id);

    UserDTO update(UserDTO user);

}
