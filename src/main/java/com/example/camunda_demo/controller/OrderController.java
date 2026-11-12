package com.example.camunda_demo.controller;

import com.example.camunda_demo.dto.OrderRequest;
import com.example.camunda_demo.dto.OrderResponse;
import com.example.camunda_demo.dto.ProdukRequest;
import com.example.camunda_demo.dto.ProdukResponse;
import com.example.camunda_demo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // =========================================================================
    // ENDPOINT ORDERS (Pelanggan & Petugas/Admin)
    // =========================================================================

    /**
     * Petugas / Admin memvalidasi stok pesanan.
     * Menggunakan AI untuk menghasilkan notifikasi konfirmasi atau permohonan maaf.
     */
    @PutMapping("/api/orders/{id}/validasi")
    public ResponseEntity<OrderResponse> validasiStok(@PathVariable Long id) {
        OrderResponse response = orderService.validasiStokPesanan(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Pelanggan membuat pesanan baru.
     */
    @PostMapping("/api/orders")
    public ResponseEntity<OrderResponse> buatPesanan(@RequestBody OrderRequest request) {
        OrderResponse response = orderService.buatPesanan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Pelanggan / Admin melihat seluruh daftar pesanan.
     */
    @GetMapping("/api/orders")
    public ResponseEntity<List<OrderResponse>> getAllPesanan() {
        return ResponseEntity.ok(orderService.getAllPesanan());
    }

    /**
     * Pelanggan / Admin melihat detail status pesanan berdasarkan ID.
     */
    @GetMapping("/api/orders/{id}")
    public ResponseEntity<OrderResponse> getPesananById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getPesananById(id));
    }

    // =========================================================================
    // ENDPOINT PRODUCTS (Petugas / Admin)
    // =========================================================================

    /**
     * Melihat seluruh katalog produk & stok.
     */
    @GetMapping("/api/products")
    public ResponseEntity<List<ProdukResponse>> getAllProduk() {
        return ResponseEntity.ok(orderService.getAllProduk());
    }

    /**
     * Petugas / Admin menambahkan produk baru.
     */
    @PostMapping("/api/products")
    public ResponseEntity<ProdukResponse> tambahProduk(@RequestBody ProdukRequest request) {
        ProdukResponse response = orderService.tambahProduk(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Petugas / Admin memperbarui data & stok produk berdasarkan ID.
     */
    @PutMapping("/api/products/{id}")
    public ResponseEntity<ProdukResponse> updateProduk(@PathVariable Long id, @RequestBody ProdukRequest request) {
        ProdukResponse response = orderService.updateProduk(id, request);
        return ResponseEntity.ok(response);
    }
}

