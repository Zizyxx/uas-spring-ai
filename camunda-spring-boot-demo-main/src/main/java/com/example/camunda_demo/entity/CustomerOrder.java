package com.example.camunda_demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_orders")
public class CustomerOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemDetail;

    @Column(columnDefinition = "TEXT")
    private String aiRecommendation;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public CustomerOrder() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getItemDetail() { return itemDetail; }
    public void setItemDetail(String itemDetail) { this.itemDetail = itemDetail; }

    public String getAiRecommendation() { return aiRecommendation; }
    public void setAiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}