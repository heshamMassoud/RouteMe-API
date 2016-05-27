package com.routeme.service;

import com.routeme.dto.SearchResponseDTO;

public interface SearchService {

    SearchResponseDTO search(String origin, String destination, String userId);
}
