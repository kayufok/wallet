package com.rickyfok.blockchain.wallet.repository;

import com.rickyfok.blockchain.wallet.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    public List<Address> findAllByOrderByIdAsc();

    public List<Address> findByAddressIn(List<String> addressList);

    public List<Address> findByAddressNotIn(List<String> addressList);

    public int countByAddress(String address);

}