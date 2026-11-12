package com.example.camunda_demo.dto;

public class OrderRequest {
    private String namaPelanggan;
    private Long produkId;
    private Integer jumlah;

    public OrderRequest() {
    }

    public OrderRequest(String namaPelanggan, Long produkId, Integer jumlah) {
        this.namaPelanggan = namaPelanggan;
        this.produkId = produkId;
        this.jumlah = jumlah;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public Long getProdukId() {
        return produkId;
    }

    public void setProdukId(Long produkId) {
        this.produkId = produkId;
    }

    public Integer getJumlah() {
        return jumlah;
    }

    public void setJumlah(Integer jumlah) {
        this.jumlah = jumlah;
    }
}

