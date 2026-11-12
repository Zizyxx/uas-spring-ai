package com.example.camunda_demo.dto;

import java.time.LocalDateTime;

public class ChatResponse {
    private String pertanyaan;
    private String jawabanAi;
    private LocalDateTime waktu;

    public ChatResponse() {
        this.waktu = LocalDateTime.now();
    }

    public ChatResponse(String pertanyaan, String jawabanAi, LocalDateTime waktu) {
        this.pertanyaan = pertanyaan;
        this.jawabanAi = jawabanAi;
        this.waktu = waktu;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getJawabanAi() {
        return jawabanAi;
    }

    public void setJawabanAi(String jawabanAi) {
        this.jawabanAi = jawabanAi;
    }

    public LocalDateTime getWaktu() {
        return waktu;
    }

    public void setWaktu(LocalDateTime waktu) {
        this.waktu = waktu;
    }
}

