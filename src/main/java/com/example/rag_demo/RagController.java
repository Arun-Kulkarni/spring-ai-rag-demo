package com.example.rag_demo;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rag")
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    // Ingest documents
    @PostMapping("/ingest")
    public Map<String, String> ingest(@RequestBody List<String> texts) {
        ragService.ingestDocuments(texts);
        return Map.of("status", "Documents ingested successfully!");
    }
    
    @PostMapping(value = "/ingest", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Map<String, String> ingestPDF(@RequestParam("files") List<MultipartFile> files) {
        ragService.ingestPdfDocuments(files);
        return Map.of("status", "Documents ingested successfully!");
    }

    // Ask a question
    @GetMapping("/ask")
    public Map<String, String> ask(@RequestParam String question) {
        String answer = ragService.ask(question);
        return Map.of("question", question, "answer", answer);
    }
}