package org.example.service;

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
    public ClaimStatus checkClaim(Map<String, String> specs, String claim) {

        String apiKey = getApiKey();
        OpenAIClient client = getOpenAIClient(apiKey);
        ChatCompletionCreateParams.Builder chatBuilder =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4_1_NANO);

        ChatCompletionCreateParams chat = chatBuilder.addSystemMessage("""
                You are an advance Seller's claim checker.
                You **must** return just "Accept" or "Reject"
               
                """
        ).addUserMessage("""
                        Is the claim valid based on the product specifications?
                        {
                            "product_specs": {
                              "name": "هدفون Sony WH-1000XM5",
                              "battery": "up to 30 hours"
                            },
                            "claim": "هدفون Sony WH-1000XM5 با عمر باتری حداکثر ۳۰ ساعت و بلوتوث ۵.۲، برای سفرهای طولانی عالیه."
                          }
                        """)
                .build();

        ChatCompletion chatCompletion = client.chat().completions().create(chatBuilder.build());

        chatCompletion.choices().get(0).message().content().ifPresent(System.out::println);

        return ClaimStatus.ACCEPT;
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
