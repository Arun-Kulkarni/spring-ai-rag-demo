package com.example.rag_demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final SimpleVectorStore vectorStore;

    public RagService(ChatClient.Builder chatClientBuilder,
                      SimpleVectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
    }

    // 1. Ingest documents into vector store
    public void ingestDocuments(List<String> texts) {
        List<Document> documents = texts.stream()
                .map(Document::new)
                .collect(Collectors.toList());
        vectorStore.add(documents);
        System.out.println("✅ Ingested " + documents.size() + " documents");
    }

    // 2. RAG - Search + Ask
    public String ask(String question) {
        // Step 1: Find relevant docs
        List<Document> relevantDocs = vectorStore.similaritySearch(
                SearchRequest.builder().query(question).topK(3).build()
        );

        // Step 2: Build context from docs
        String context = relevantDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        // Step 3: Send to LLM with context
        String prompt = """
                Use the following context to answer the question.
                If you don't know the answer, say "I don't know".
                
                Context:
                %s
                
                Question: %s
                """.formatted(context, question);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
    
    public void ingestPdfDocuments(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            try {
                var resource = new InputStreamResource(file.getInputStream());

                TikaDocumentReader tikaReader = new TikaDocumentReader(resource);
                List<Document> documents = tikaReader.get();

                TokenTextSplitter splitter = new TokenTextSplitter();
                List<Document> chunks = splitter.apply(documents);

                vectorStore.add(chunks);

            } catch (IOException e) {
                throw new RuntimeException("Failed to process PDF: " + file.getOriginalFilename(), e);
            }
        }
    }
}