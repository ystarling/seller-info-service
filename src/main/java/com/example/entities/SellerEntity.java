package com.example.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "sellers")
public class SellerEntity {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "producer_id", nullable = false)
    private ProducerEntity producer;

    @ManyToOne
    @JoinColumn(name = "seller_info_id")
    private SellerInfoEntity sellerInfo;

    @Column(name = "state", nullable = false)
    private String state;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ProducerEntity getProducer() {
        return producer;
    }

    public void setProducer(ProducerEntity producer) {
        this.producer = producer;
    }

    public SellerInfoEntity getSellerInfo() {
        return sellerInfo;
    }

    public void setSellerInfo(SellerInfoEntity sellerInfo) {
        this.sellerInfo = sellerInfo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "SellerEntity{" +
                "id=" + id +
                ", producer=" + producer +
                ", sellerInfo=" + sellerInfo +
                ", state='" + state + '\'' +
                '}';
    }
}

