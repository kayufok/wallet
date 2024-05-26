package com.rickyfok.blockchain.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "log_eth")
@Data
public class LogEth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long statusId;

    private String message;

}