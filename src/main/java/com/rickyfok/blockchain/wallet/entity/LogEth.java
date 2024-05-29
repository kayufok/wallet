package com.rickyfok.blockchain.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "log_eth")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class LogEth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long statusId;

    private String message;

}