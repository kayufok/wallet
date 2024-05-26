package com.rickyfok.blockchain.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "address_eth")
@Data
public class AddressEth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long addressId;

}
