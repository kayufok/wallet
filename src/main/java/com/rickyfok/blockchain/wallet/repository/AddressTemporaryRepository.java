package com.rickyfok.blockchain.wallet.repository;

import com.rickyfok.blockchain.wallet.entity.AddressTemporary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressTemporaryRepository extends JpaRepository<AddressTemporary, Long> {
}
