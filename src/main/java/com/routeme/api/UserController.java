package com.routeme.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.routeme.dto.UserDTO;
import com.routeme.model.Route;
import com.routeme.predictionio.PredictionIOClient;
import com.routeme.service.UserService;

@RestController
@RequestMapping(value = "/api/users")
public final class UserController {
    private final UserService service;
    private PredictionIOClient predictionIOClient;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
        this.predictionIOClient = new PredictionIOClient();
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    List<UserDTO> findAll() {
        return service.findAll();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO create(@RequestBody @Valid UserDTO userEntry) {
        UserDTO userRecord = service.create(userEntry);
        String userId = userRecord.getId();
        predictionIOClient.addUserToClient(userId);
        predictionIOClient.preferRouteType(userId, PredictionIOClient.PREFERENCE_ROUTE_TYPE_LEASTTIME);
        predictionIOClient.preferRouteMode(userId, PredictionIOClient.PREFERENCE_ROUTE_MODE_BUS);
        predictionIOClient.preferRouteMode(userId, PredictionIOClient.PREFERENCE_ROUTE_MODE_BUS);
        predictionIOClient.preferRouteMode(userId, PredictionIOClient.PREFERENCE_ROUTE_MODE_UBAHN);
        return userRecord;
    }

    @RequestMapping(value = "{id}/take/{routeId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    void takeRoute(@PathVariable("id") String userId, @PathVariable("routeId") String routeId) {
        predictionIOClient.takeRoute(userId, routeId);
    }

    @RequestMapping(value = "{id}/view/{routeId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    void viewRoute(@PathVariable("id") String userId, @PathVariable("routeId") String routeId) {
        predictionIOClient.viewRoute(userId, routeId);
    }

    @RequestMapping(value = "{id}/viewlast/{routeId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    void ViewRouteLast(@PathVariable("id") String userId, @PathVariable("routeId") String routeId) {
        predictionIOClient.viewRouteLast(userId, routeId);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json")
    UserDTO delete(@PathVariable("id") String id) {
        return service.delete(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    UserDTO findById(@PathVariable("id") String id) {
        return service.findById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
    UserDTO update(@RequestBody @Valid UserDTO userEntry, @PathVariable("id") String id) {
        // TODO: Need not send whole user entity. For the user case of needing
        // to update one field.
        String password = userEntry.getPassword();
        String[] passwordSplit = password.split(",");
        String startPoint = passwordSplit[0].trim();
        ArrayList<String> transportations = new ArrayList<String>();
        for (int i = 1; i < passwordSplit.length - 2; i++) {
            transportations.add(passwordSplit[i].trim());
        }
        String endPoint = passwordSplit[passwordSplit.length - 2].trim();
        String routeType = passwordSplit[passwordSplit.length - 1].trim();
        Route newRoute = Route.getFactory().startPoint(startPoint).transportations(transportations).endPoint(endPoint)
                .routeType(routeType).build();
        predictionIOClient.addRouteToClient(newRoute);
        return service.update(userEntry);
    }

}
