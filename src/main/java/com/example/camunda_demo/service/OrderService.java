package com.example.camunda_demo.service;

import com.example.camunda_demo.dto.OrderRequest;
import com.example.camunda_demo.dto.OrderResponse;
import com.example.camunda_demo.dto.ProdukRequest;
import com.example.camunda_demo.dto.ProdukResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private final ChatClient chatClient;

    // =========================================================================
    // SIMULASI DATA IN-MEMORY (Sementara untuk Orang 1)
    // TODO (Orang 2): Ganti struktur data in-memory ini dengan JPA Repository
    // (ProdukRepository dan PesananRepository) saat branch Orang 2 digabungkan.
    // =========================================================================
    private final Map<Long, ProdukResponse> produkMap = new ConcurrentHashMap<>();
    private final Map<Long, OrderResponse> orderMap = new ConcurrentHashMap<>();
    private final AtomicLong produkIdGenerator = new AtomicLong(1);
    private final AtomicLong orderIdGenerator = new AtomicLong(1);

    public OrderService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
        initSampleData();
    }

    private void initSampleData() {
        // Data awal simulasi produk
        simpanProdukInternal(new ProdukResponse(produkIdGenerator.getAndIncrement(), "Laptop Asus Vivobook", "Elektronik", 8500000.0, 5));
        simpanProdukInternal(new ProdukResponse(produkIdGenerator.getAndIncrement(), "Mouse Logitech Wireless", "Aksesoris", 150000.0, 10));
        simpanProdukInternal(new ProdukResponse(produkIdGenerator.getAndIncrement(), "Keyboard Mechanical RGB", "Aksesoris", 450000.0, 0)); // Stok 0 untuk simulasi stok habis
        simpanProdukInternal(new ProdukResponse(produkIdGenerator.getAndIncrement(), "Monitor LG 24 Inch", "Elektronik", 1750000.0, 3));

        // Data awal simulasi pesanan
        simpanOrderInternal(new OrderResponse(orderIdGenerator.getAndIncrement(), "Budi Santoso", "Laptop Asus Vivobook", 1, "MENUNGGU VALIDASI", "-"));
        simpanOrderInternal(new OrderResponse(orderIdGenerator.getAndIncrement(), "Siti Rahma", "Keyboard Mechanical RGB", 2, "MENUNGGU VALIDASI", "-"));
    }

    private void simpanProdukInternal(ProdukResponse p) {
        produkMap.put(p.getId(), p);
    }

    private void simpanOrderInternal(OrderResponse o) {
        orderMap.put(o.getOrderId(), o);
    }

    // =========================================================================
    // 1. VALIDASI STOK PESANAN (Petugas / Admin)
    // =========================================================================
    public OrderResponse validasiStokPesanan(Long orderId) {
        // TODO (Orang 2): Ganti dengan PesananRepository.findById(orderId)
        OrderResponse order = orderMap.get(orderId);
        if (order == null) {
            throw new NoSuchElementException("Pesanan dengan ID " + orderId + " tidak ditemukan!");
        }

        // Cari data produk terkait untuk validasi stok
        // TODO (Orang 2): Ganti dengan ProdukRepository.findByNamaProduk(...) atau relasi @ManyToOne di Pesanan
        ProdukResponse produkTerkait = produkMap.values().stream()
                .filter(p -> p.getNamaProduk().equalsIgnoreCase(order.getNamaProduk()))
                .findFirst()
                .orElse(null);

        boolean stokAda = false;
        if (produkTerkait != null && produkTerkait.getStok() >= order.getJumlah()) {
            stokAda = true;
        }

        String catatanAi;
        if (stokAda) {
            // Stok mencukupi -> Kurangi stok & proses pesanan
            produkTerkait.setStok(produkTerkait.getStok() - order.getJumlah());
            // TODO (Orang 2): Ganti dengan ProdukRepository.save(produkTerkait)

            order.setStatus("PESANAN DIPROSES");

            // AI membuat pesan konfirmasi pesanan diproses
            String promptKonfirmasi = String.format(
                    "Buat pesan konfirmasi pesanan diproses untuk pelanggan bernama %s yang memesan %d unit %s. " +
                    "Gunakan nada ramah, berikan apresiasi, dan sampaikan bahwa barang segera disiapkan.",
                    order.getNamaPelanggan(), order.getJumlah(), order.getNamaProduk()
            );

            catatanAi = chatClient.prompt()
                    .user(promptKonfirmasi)
                    .call()
                    .content();
        } else {
            // Stok tidak mencukupi -> Batalkan pesanan
            order.setStatus("PESANAN DIBATALKAN");

            // AI membuat pesan permohonan maaf stok habis
            String promptBatal = String.format(
                    "Buat pesan permohonan maaf stok habis kepada pelanggan bernama %s untuk pesanan %d unit %s. " +
                    "Gunakan nada sopan dan empatik, sampaikan permohonan maaf secara profesional, dan sarankan alternatif produk bila relevan.",
                    order.getNamaPelanggan(), order.getJumlah(), order.getNamaProduk()
            );

            catatanAi = chatClient.prompt()
                    .user(promptBatal)
                    .call()
                    .content();
        }

        order.setCatatanAi(catatanAi);

        // TODO (Orang 2): Simpan perubahan via PesananRepository.save(orderEntity)
        orderMap.put(order.getOrderId(), order);

        return order;
    }

    // =========================================================================
    // 2. KELOLA PESANAN (Pelanggan & Admin)
    // =========================================================================
    public OrderResponse buatPesanan(OrderRequest request) {
        // TODO (Orang 2): Ganti dengan ProdukRepository.findById(request.getProdukId())
        ProdukResponse produk = produkMap.get(request.getProdukId());
        if (produk == null) {
            throw new NoSuchElementException("Produk dengan ID " + request.getProdukId() + " tidak ditemukan!");
        }

        OrderResponse baru = new OrderResponse();
        baru.setOrderId(orderIdGenerator.getAndIncrement());
        baru.setNamaPelanggan(request.getNamaPelanggan());
        baru.setNamaProduk(produk.getNamaProduk());
        baru.setJumlah(request.getJumlah());
        baru.setStatus("MENUNGGU VALIDASI");
        baru.setCatatanAi("Pesanan baru masuk, menunggu validasi stok oleh petugas.");

        // TODO (Orang 2): Simpan ke MySQL menggunakan PesananRepository.save(...)
        orderMap.put(baru.getOrderId(), baru);

        return baru;
    }

    public List<OrderResponse> getAllPesanan() {
        // TODO (Orang 2): Ganti dengan PesananRepository.findAll()
        return new ArrayList<>(orderMap.values());
    }

    public OrderResponse getPesananById(Long id) {
        // TODO (Orang 2): Ganti dengan PesananRepository.findById(id)
        OrderResponse order = orderMap.get(id);
        if (order == null) {
            throw new NoSuchElementException("Pesanan dengan ID " + id + " tidak ditemukan!");
        }
        return order;
    }

    // =========================================================================
    // 3. KELOLA DATA PRODUK & STOK (Petugas / Admin)
    // =========================================================================
    public List<ProdukResponse> getAllProduk() {
        // TODO (Orang 2): Ganti dengan ProdukRepository.findAll()
        return new ArrayList<>(produkMap.values());
    }

    public ProdukResponse tambahProduk(ProdukRequest request) {
        ProdukResponse produk = new ProdukResponse(
                produkIdGenerator.getAndIncrement(),
                request.getNamaProduk(),
                request.getKategori(),
                request.getHarga(),
                request.getStok()
        );

        // TODO (Orang 2): Ganti dengan ProdukRepository.save(produkEntity)
        produkMap.put(produk.getId(), produk);
        return produk;
    }

    public ProdukResponse updateProduk(Long id, ProdukRequest request) {
        // TODO (Orang 2): Ganti dengan ProdukRepository.findById(id)
        ProdukResponse produk = produkMap.get(id);
        if (produk == null) {
            throw new NoSuchElementException("Produk dengan ID " + id + " tidak ditemukan!");
        }

        produk.setNamaProduk(request.getNamaProduk());
        produk.setKategori(request.getKategori());
        produk.setHarga(request.getHarga());
        produk.setStok(request.getStok());

        // TODO (Orang 2): Ganti dengan ProdukRepository.save(produk)
        produkMap.put(id, produk);
        return produk;
    }
}

