package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.example.enums.ClaimStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class LlmServiceImpl implements LlmService {

    @Override
    public ClaimStatus
    checkClaim(Map<String, String> specs, String claim) {

        String apiKey = getApiKey();
        OpenAIClient client = getOpenAIClient(apiKey);
        ChatCompletionCreateParams.Builder chatBuilder =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4_1_NANO);

        String specsJson = "";
        try {
            specsJson = new ObjectMapper().writeValueAsString(specs);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String systemMessage = """
                You are an advance Seller's claim checker.
                You **must** return just "Accept" or "Reject"
                """;

        String userMessage = String.format("""
                        Is the claim valid based on the product specifications?
                        {
                            "product_specs": %s,
                            "claim": %s"
                        }
                        """, specsJson, claim);

        ChatCompletionCreateParams chat = chatBuilder.addSystemMessage(systemMessage)
                .addUserMessage(userMessage)
                .build();

        ChatCompletion chatCompletion = client.chat().completions().create(chat);
        try {
            return Enum.valueOf(ClaimStatus.class, chatCompletion.choices().get(0).message().content().get().toUpperCase());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid day provided: " + chatCompletion.choices().get(0).message().content().get());
        }
        return ClaimStatus.REJECT;
    }

    @NotNull
    private String getApiKey() {
        String apiKey = System.getenv("LLM_COURSE_OPENROUTER_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            System.err.println("Error: LLM_COURSE_OPENROUTER_API_KEY environment variable is not set");
            System.exit(1);
        }
        return apiKey;
    }

    @NotNull
    private static OpenAIClient getOpenAIClient(String apiKey) {
        return OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .baseUrl("https://openrouter.ai/api/v1")
                .build();
    }
}
