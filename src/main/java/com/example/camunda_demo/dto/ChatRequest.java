package com.example.camunda_demo.dto;

public class ChatRequest {
    private String pertanyaan;

    public ChatRequest() {
    }

    public ChatRequest(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }
}

