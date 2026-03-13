# Spring AI RAG Demo

This repository showcases the API endpoints for text ingestion and question asking using Spring AI RAG.

## API Endpoints

### 1. Text Ingestion
- **Endpoint:** `POST /rag/ingest`
- **Description:** Ingests plain text for processing.

### 2. PDF File Ingestion
- **Endpoint:** `POST /rag/ingest`
- **Description:** Ingests PDF files using multipart/form-data.

### 3. Question Asking
- **Endpoint:** `GET /rag/ask`
- **Description:** Retrieve answers based on provided questions.