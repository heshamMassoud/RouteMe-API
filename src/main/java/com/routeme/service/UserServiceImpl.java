package com.routeme.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.routeme.dto.EventDTO;
import com.routeme.dto.UserDTO;
import com.routeme.model.RouteEntity;
import com.routeme.model.User;
import com.routeme.repository.RouteRepository;
import com.routeme.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RouteRepository routeRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RouteRepository routeRepository) {
        this.userRepository = userRepository;
        this.routeRepository = routeRepository;
    }

    @Override
    public Optional<UserDTO> create(UserDTO userEntry) {
        if (userExists(userEntry.getEmail())) {
            return Optional.empty();
        }
        User persistedUser = User.getFactory().name(userEntry.getUsername()).email(userEntry.getEmail())
                .password(userEntry.getPassword()).routeTypePreference(userEntry.getRouteTypePreference())
                .travelModePreference(userEntry.getTravelModePreference()).build();
        persistedUser = userRepository.save(persistedUser);
        return Optional.of(convertToDTO(persistedUser));
    }

    @Override
    public UserDTO delete(String id) {
        User deletedUser = findUserById(id);
        userRepository.delete(deletedUser);
        return convertToDTO(deletedUser);
    }

    @Override
    public List<UserDTO> findAll() {
        List<User> userEntries = userRepository.findAll();
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
        updatedUser.update(userEntry.getUsername(), userEntry.getEmail(), userEntry.getPassword());
        updatedUser = userRepository.save(updatedUser);
        return convertToDTO(updatedUser);
    }

    private User findUserById(String id) {
        Optional<User> result = userRepository.findOne(id);
        return result.get();
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRouteTypePreference(user.getRouteTypePreference());
        dto.setTravelModePreference(user.getTravelModePreference());
        ArrayList<String> likedRoutePioIds = new ArrayList<String>();
        List<RouteEntity> likedRoutes = user.getRoutesLiked();
        if (likedRoutes != null) {
            for (RouteEntity route : likedRoutes) {
                likedRoutePioIds.add(route.getPioId());
            }
            dto.setLikedRoutes(likedRoutePioIds);
        }
        return dto;
    }

    private boolean userExists(String email) {
        List<User> result = userRepository.findByEmail(email);
        return !result.isEmpty();
    }

    @Override
    public Optional<UserDTO> findByCredentials(String email, String password) {
        List<User> result = userRepository.findByEmailAndPassword(email, password);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        UserDTO userDto = convertToDTO(result.get(0));
        return Optional.of(userDto);
    }

    @Override
    public UserDTO likeRoute(EventDTO eventEntry) {
        User liker = findUserById(eventEntry.getUserId());
        String pioId = eventEntry.getTargetEntityId();
        Optional<RouteEntity> route = findRouteByPioId(pioId);
        if (route.isPresent()) {
            persistRouteAndLiker(route.get(), liker);
        } else {
            RouteEntity persistedRoute = createRoute(pioId);
            persistRouteAndLiker(persistedRoute, liker);
        }
        return convertToDTO(liker);
    }

    private void persistRouteAndLiker(RouteEntity route, User liker) {
        liker.addlikedRoute(route);
        userRepository.save(liker);
    }

    private RouteEntity createRoute(String pioId) {
        RouteEntity persistedRoute = RouteEntity.getFactory().pioId(pioId).build();
        persistedRoute = routeRepository.save(persistedRoute);
        return persistedRoute;
    }

    private Optional<RouteEntity> findRouteByPioId(String pioId) {
        Optional<RouteEntity> result = routeRepository.findByPioId(pioId);
        return result;
    }

    @Override
    public UserDTO setPreferences(UserDTO preferencesEntry) {
        User user = findUserById(preferencesEntry.getId());
        user.setRouteTypePreference(preferencesEntry.getRouteTypePreference());
        user.setTravelModePreference(preferencesEntry.getTravelModePreference());
        user = userRepository.save(user);
        return convertToDTO(user);
    }

}
