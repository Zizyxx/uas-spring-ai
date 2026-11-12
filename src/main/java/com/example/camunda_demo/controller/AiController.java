package com.example.camunda_demo.controller;

import com.example.camunda_demo.dto.ChatRequest;
import com.example.camunda_demo.dto.ChatResponse;
import com.example.camunda_demo.service.OrderAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AiController {

    private final OrderAiService orderAiService;

    public AiController(OrderAiService orderAiService) {
        this.orderAiService = orderAiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        ChatResponse response = orderAiService.prosesPertanyaan(request.getPertanyaan());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/riwayat")
    public ResponseEntity<List<ChatResponse>> getRiwayatChat() {
        return ResponseEntity.ok(orderAiService.getRiwayatChat());
    }
}

