package com.rickyfok.blockchain.wallet.repository;

import com.rickyfok.blockchain.wallet.entity.LogEthereum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogEthereumRepository extends JpaRepository<LogEthereum, Long> {
    List<LogEthereum> findByStatusId(int statusId, org.springframework.data.domain.Pageable pageable);
}
