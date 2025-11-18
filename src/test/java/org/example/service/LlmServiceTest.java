package org.example.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.enums.ClaimStatus;
import org.example.service.dto.ClaimTestCase;
import org.example.service.dto.EvaluationResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

class LlmServiceTest {

    private final LlmService llmService = new LlmServiceImpl();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final EvaluationResult evaluationResult = new EvaluationResult();
    private static int totalCasesCount;
    private static int failedCasesCount;

    static Stream<ClaimTestCase> jsonProvider() throws Exception {
        InputStream inputStream = LlmServiceTest.class.getResourceAsStream("/claim-tests.json");
        List<ClaimTestCase> list = mapper.readValue(inputStream, new TypeReference<>() {});
        return list.stream();
    }

    @AfterAll
    static void afterTest() {
        double accuracy = (double) (totalCasesCount - failedCasesCount) / totalCasesCount * 100;
        System.out.println("Accuracy: " + accuracy);
        evaluationResult.getFailedCases().forEach(System.out::println);
    }

    @ParameterizedTest(name = "Test case: {index} – claim: {0}")
    @MethodSource("jsonProvider")
    void checkClaim(ClaimTestCase tc) {
        ClaimStatus result = llmService.checkClaim(tc.getProduct_specs(), tc.getClaim());

        if (!result.equals(tc.getLabel())) {
            evaluationResult.getFailedCases().add(tc);
            failedCasesCount++;
        }

        totalCasesCount++;

        Assertions.assertEquals(result, tc.getLabel());
    }
}