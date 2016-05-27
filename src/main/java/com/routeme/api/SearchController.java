package com.routeme.api;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.routeme.dto.SearchRequestDTO;
import com.routeme.dto.SearchResponseDTO;
import com.routeme.service.SearchService;

@RestController
@RequestMapping(value = "/api/search")
public class SearchController {
    private final SearchService service;

    @Autowired
    public SearchController(SearchService service) {
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    SearchResponseDTO search(@RequestBody @Valid SearchRequestDTO searchRequest) {
        String origin = searchRequest.getStartPoint();
        String destination = searchRequest.getEndPoint();
        String userId = searchRequest.getUserId();
        return service.search(origin, destination, userId);
    }
}
