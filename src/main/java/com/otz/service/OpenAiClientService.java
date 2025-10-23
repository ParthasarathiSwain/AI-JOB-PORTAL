package com.otz.service;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenAiClientService {

    private final OpenAiService openAiService;

    public OpenAiClientService() {
        // Replace with your OpenAI API Key
        this.openAiService = new OpenAiService("Your Key");
    }

    // Generate summary from resume
    public String generateSummary(String resumeText) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        new ChatMessage("system", "You are an AI resume analyzer."),
                        new ChatMessage("user", "Summarize and highlight key skills from this resume:\n" + resumeText)
                ))
                .build();

        ChatCompletionChoice choice = openAiService.createChatCompletion(request)
                .getChoices().get(0);
        return choice.getMessage().getContent();
    }

    // Compute semantic match between resume & job description
    public double computeSemanticMatch(String resumeText, String jobDescription) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(List.of(
                        new ChatMessage("system", "You are an AI job matching assistant."),
                        new ChatMessage("user", "Rate the match between this resume and job description from 0 to 100.\nResume:\n"
                                + resumeText + "\nJob:\n" + jobDescription)
                ))
                .build();

        String response = openAiService.createChatCompletion(request)
                .getChoices().get(0).getMessage().getContent();

        try {
            return Double.parseDouble(response.replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }
}
