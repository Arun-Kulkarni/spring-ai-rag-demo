# API Documentation

## Overview
This project provides an API for text and PDF file ingestion and question answering using the RAG (Retrieval-Augmented Generation) model.

## Endpoints

### POST /rag/ingest
- **Description**: Upload text and PDF files for processing.
- **Request Body**: 
  - `file`: The file to be uploaded (can be text or PDF).
- **Response**: 
  - `200 OK`: The file has been successfully ingested.
  - `400 Bad Request`: Invalid file type or other errors.

### GET /rag/ask
- **Description**: Ask a question using the ingested data.
- **Request Parameters**: 
  - `question`: The question to be answered based on the ingested files.
- **Response**: 
  - `200 OK`: The answer to the question.
  - `400 Bad Request`: Invalid question format.
  - `404 Not Found`: No data available to answer the question.

## Project Details
- **Owner**: Arun-Kulkarni
- **Repository**: spring-ai-rag-demo