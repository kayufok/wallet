package com.rickyfok.blockchain.wallet.repository;

import com.rickyfok.blockchain.wallet.entity.LogEth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEthRepository extends JpaRepository<LogEth, Long> {

    // select max(id) from log_eth by jql
   @Query("SELECT MAX(l.id) FROM LogEth l")
    Long findMaxId();

}
