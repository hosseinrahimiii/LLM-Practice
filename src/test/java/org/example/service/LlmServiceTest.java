package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.enums.ClaimStatus;
import org.example.service.dto.ClaimTestCase;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LlmServiceTest {

    private final LlmService llmService = new LlmServiceImpl();
    private static final ObjectMapper mapper = new ObjectMapper();

    static Stream<ClaimTestCase> jsonProvider() throws Exception {
        InputStream inputStream = LlmServiceTest.class.getResourceAsStream("claim-tests.json");
        List<ClaimTestCase> list = mapper.readValue(inputStream, new TypeReference<>() {});
        return list.stream();
    }

    @ParameterizedTest(name = "Test case: {index} – claim: {0.claim}")
    @MethodSource("jsonProvider")
    void checkClaim(ClaimTestCase tc) {
        ClaimStatus result = llmService.checkClaim(tc.getProduct_specs(), tc.getClaim());

        assertEquals(tc.getLabel(), result);
    }
}