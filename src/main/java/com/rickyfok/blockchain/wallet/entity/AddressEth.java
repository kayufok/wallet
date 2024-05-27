package com.rickyfok.blockchain.wallet.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Entity
@Table(name = "address_eth")
@Data
@Accessors(chain = true)
public class AddressEth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long addressId;

}
