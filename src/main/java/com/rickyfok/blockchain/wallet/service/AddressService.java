package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.repository.AddressRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
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

    public List<Address> saveAddressList(List<String> addressStringList) throws SQLIntegrityConstraintViolationException {
        List<Address> addressList = new ArrayList<>();

        if (addressStringList.isEmpty()) return null;

        addressStringList.forEach(a -> {
            Address address = new Address();
            address.setAddress(a);
            addressList.add(address);
        });
        return addressRepository.saveAll(addressList);
    }

//    public List<Address> createAddress(List<String> addressList) {
//        Address existingAddressList = addressRepository.findByAddress(address);
//        if (existingAddress != null) {
//            return null; // Address already exists, return it
//        }
//        Address newAddress = new Address(address);
//        addressRepository.save(newAddress);
//        return ResponseEntity.status(HttpStatus.CREATED).body(newAddress);
//    }
}
