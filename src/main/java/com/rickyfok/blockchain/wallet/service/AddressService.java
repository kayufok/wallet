package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public List<Address> getAddressList() {
        return addressRepository.findAllByOrderByIdAsc();
    }

    public int getAddressCount(String address) {
        return addressRepository.countByAddress(address);
    }


    public List<Address> getAddressListExist(List<String> addressList) {
        return addressRepository.findByAddressIn(addressList);
    }

    public List<Address> getAddressListNotExist(List<String> addressList) {
        return addressRepository.findByAddressNotIn(addressList);
    }

    public Address saveAddress(Address address){
        return addressRepository.save(address);
    };

    public List<Address> saveAddressList(List<String> addressStringList) throws Exception {

        return addressStringList
                .stream()
                .parallel()
                .filter(a -> !a.isEmpty())
                .map(a ->  new Address(null, a))  // new Address
                .map(a -> addressRepository.save(a)) // return saved address
                .peek( a -> System.out.println(Thread.currentThread().getName() + "address :" + a.getAddress()))
                .toList();
    }

}
