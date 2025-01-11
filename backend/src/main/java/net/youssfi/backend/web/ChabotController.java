package net.youssfi.backend.web;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;



@RestController
@CrossOrigin("*")
public class ChabotController {

    // Interface for interacting with various AI models (e.g., Llama, ChatGPT, OpenAI, etc.)
    private final ChatClient chatClient;

    // Interface for querying the vector store (Postgres database)
    private final VectorStore vectorStore;

    // Constructor to initialize the ChatClient and VectorStore
    public ChabotController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore)) // Use the vector store for question-answering
                .build();
        this.vectorStore = vectorStore;
    }

    // Endpoint to handle the chat request
    @GetMapping("/chat")
    public String chat( String message) {
        // Retrieve documents similar to the user's query
        //List<Document> documents = vectorStore.similaritySearch(message); deja fait  en .defaultAdvisors(new QuestionAnswerAdvisor(vectorStore))

        // Process the user's message and get the response from the AI model
        return chatClient.prompt()
                .user(message) // Pass the user input to the model
                .call()        // Call the AI model (e.g., GPT-4)
                .content();    // Return the model's response
    }
}
