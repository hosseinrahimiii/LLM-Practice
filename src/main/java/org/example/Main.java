package org.example;

import org.example.enums.ClaimStatus;
import org.example.service.LlmService;
import org.example.service.LlmServiceImpl;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        LlmService  llmService = new LlmServiceImpl();

        Map<String, String> specs = Map.of(
                "battery", "up to 10 hours",
                "ram", "8GB"
        );

        String claim = "The battery lasts 20 hours.";

        ClaimStatus claimStatus = llmService.checkClaim(specs, claim);
    }
}