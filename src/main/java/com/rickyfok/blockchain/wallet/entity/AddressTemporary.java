package com.rickyfok.blockchain.wallet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Table(name = "address_temporary")
@Data
@Accessors(chain = true, fluent = true)
@AllArgsConstructor
@NoArgsConstructor
public class AddressTemporary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

}