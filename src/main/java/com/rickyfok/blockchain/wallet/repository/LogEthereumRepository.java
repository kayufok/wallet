package com.rickyfok.blockchain.wallet.repository;

import com.rickyfok.blockchain.wallet.entity.LogEthereum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEthereumRepository extends JpaRepository<LogEthereum, Long> {}
