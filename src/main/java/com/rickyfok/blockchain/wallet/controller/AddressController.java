package com.rickyfok.blockchain.wallet.controller;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.service.AddressService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;


    @GetMapping("/list")
    public List<Address> getAddressList() {
        return addressService.getAddressList();
    }

    @GetMapping("/list/exist")
    public List<Address> getAddressesExist(@RequestBody List<String> addressList) {
        return addressService.getAddressListExist(addressList);
    }

    @GetMapping("/list/not-exist")
    public List<Address> getAddressesNotExist(@RequestBody List<String> addressList) {
        return addressService.getAddressListNotExist(addressList);
    }

    @PostMapping("/list")
    public ResponseEntity<Object> saveAddressList(@RequestBody List<String> addressList)  {
        try{
            return ResponseEntity.ok(addressService.saveAddressList(addressList));
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getLocalizedMessage());
        }

    }

}
