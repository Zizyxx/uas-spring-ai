package com.example.camunda_demo.dto;

public class OrderResponse {
    private Long orderId;
    private String namaPelanggan;
    private String namaProduk;
    private Integer jumlah;
    private String status;
    private String catatanAi;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, String namaPelanggan, String namaProduk, Integer jumlah, String status, String catatanAi) {
        this.orderId = orderId;
        this.namaPelanggan = namaPelanggan;
        this.namaProduk = namaProduk;
        this.jumlah = jumlah;
        this.status = status;
        this.catatanAi = catatanAi;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCatatanAi() {
        return catatanAi;
    }

    public void setCatatanAi(String catatanAi) {
        this.catatanAi = catatanAi;
    }
}

