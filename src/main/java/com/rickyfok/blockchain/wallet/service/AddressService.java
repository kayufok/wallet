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

    public List<Address> getAddressListExist(List<String> addressList) {
        return addressRepository.findByAddressIn(addressList);
    }

    public List<Address> getAddressListNotExist(List<String> addressList) {
        return addressRepository.findByAddressNotIn(addressList);
    }

    public List<Address> saveAddressList(List<String> addressStringList) throws Exception {
        List<Address> addressList = new ArrayList<>();

        if (addressStringList.isEmpty()) return null;

        addressStringList.forEach(a -> {
            Address address = new Address();
            address.setAddress(a);
            addressList.add(address);
        });
        return addressRepository.saveAll(addressList);
    }

    public void saveAddress(Address address) throws Exception {
        addressRepository.save(address);
    };

}
