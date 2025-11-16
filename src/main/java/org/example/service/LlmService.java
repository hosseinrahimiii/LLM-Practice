package org.example.service;

import org.example.enums.ClaimStatus;

import java.util.Map;

public interface LlmService {

    ClaimStatus checkClaim(Map<String, String> specs, String claim);

}
