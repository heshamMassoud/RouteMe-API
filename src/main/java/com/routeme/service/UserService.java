package com.routeme.service;

import java.util.List;
import java.util.Optional;

import com.routeme.dto.EventDTO;
import com.routeme.dto.UserDTO;
import com.routeme.predictionio.PredictionIOClient;

public interface UserService {

    Optional<UserDTO> create(UserDTO user);

    Optional<UserDTO> findByCredentials(String email, String password);

    UserDTO delete(String id);

    List<UserDTO> findAll();

    UserDTO findById(String id);

    UserDTO update(UserDTO user);

    UserDTO likeRoute(EventDTO eventEntry, PredictionIOClient predictionIOClient);
    
    UserDTO dislikeRoute(EventDTO eventEntry);

    UserDTO setPreferences(UserDTO preferencesEntry, PredictionIOClient predictionIOClient);

}
