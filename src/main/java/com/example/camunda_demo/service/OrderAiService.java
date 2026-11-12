package com.example.camunda_demo.service;

import com.example.camunda_demo.dto.ChatResponse;
import com.example.camunda_demo.dto.ProdukResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderAiService {

    private final ChatClient chatClient;
    private final OrderService orderService;

    // In-memory list untuk menyimpan riwayat chat selama masa pengembangan Orang 1
    // TODO (Orang 2): Ganti in-memory list ini dengan RiwayatChatRepository (MySQL)
    private final List<ChatResponse> riwayatChatList = Collections.synchronizedList(new ArrayList<>());

    public OrderAiService(ChatClient.Builder chatClientBuilder, OrderService orderService) {
        this.chatClient = chatClientBuilder.build();
        this.orderService = orderService;
    }

    public ChatResponse prosesPertanyaan(String pertanyaan) {
        // 1. Ambil daftar produk & stok terkini
        // TODO (Orang 2): Ganti dengan ProdukRepository.findAll()
        List<ProdukResponse> daftarProduk = orderService.getAllProduk();

        // Susun ringkasan katalog produk dan stok untuk konteks AI
        StringBuilder katalogBuilder = new StringBuilder();
        for (ProdukResponse p : daftarProduk) {
            katalogBuilder.append(String.format(
                    "- [ID: %d] %s (Kategori: %s, Harga: Rp %.0f, Stok: %d unit)\n",
                    p.getId(), p.getNamaProduk(), p.getKategori(), p.getHarga(), p.getStok()
            ));
        }

        // 2. Gabungkan System Prompt, Data Stok Produk, dan Pertanyaan User
        String systemPrompt = """
                Anda adalah asisten belanja online yang ramah, sopan, dan informatif untuk Sistem Pemesanan Barang.
                Tugas Anda adalah:
                1. Memberikan rekomendasi produk yang tepat sesuai kebutuhan pelanggan.
                2. Menginformasikan ketersediaan stok produk berdasarkan data katalog terbaru di bawah ini.
                3. Jika stok produk 0 (habis), sampaikan dengan sopan bahwa barang sedang kosong dan tawarkan alternatif produk lain yang relevan dari katalog.
                4. Jawab dengan ringkas, jelas, dan selalu gunakan bahasa Indonesia yang santun.

                === KATALOG PRODUK & STOK SAAT INI ===
                """ + katalogBuilder + """
                =====================================
                """;

        String jawabanAi = chatClient.prompt()
                .system(systemPrompt)
                .user(pertanyaan)
                .call()
                .content();

        ChatResponse response = new ChatResponse(pertanyaan, jawabanAi, LocalDateTime.now());

        // 3. Simpan riwayat chat
        // TODO (Orang 2): Simpan ke MySQL menggunakan RiwayatChatRepository.save(riwayatChatEntity)
        riwayatChatList.add(response);

        return response;
    }

    public List<ChatResponse> getRiwayatChat() {
        // TODO (Orang 2): Ganti dengan RiwayatChatRepository.findAll()
        return new ArrayList<>(riwayatChatList);
    }
}

