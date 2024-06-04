package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.repository.AddressRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rickyfok.blockchain.wallet.util.Suppiler.threadName;

@Service
@Log4j2
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
                .peek(a -> log.info("{} address: {}", threadName, a.getAddress()))
                .toList();
    }

}
