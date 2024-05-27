package com.rickyfok.blockchain.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "log_eth")
@Data
@Accessors(chain = true)
public class LogEth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long statusId;

    private String message;

}