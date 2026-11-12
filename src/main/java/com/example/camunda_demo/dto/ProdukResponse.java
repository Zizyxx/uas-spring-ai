package com.example.camunda_demo.dto;

public class ProdukResponse {
    private Long id;
    private String namaProduk;
    private String kategori;
    private Double harga;
    private Integer stok;

    public ProdukResponse() {
    }

    public ProdukResponse(Long id, String namaProduk, String kategori, Double harga, Integer stok) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public Integer getStok() {
        return stok;
    }

    public void setStok(Integer stok) {
        this.stok = stok;
    }
}

