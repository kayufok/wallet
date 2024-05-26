package com.rickyfok.blockchain.wallet.repository;


import com.rickyfok.blockchain.wallet.entity.AddressEth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressEthRepository extends JpaRepository<AddressEth, Long> {
}
