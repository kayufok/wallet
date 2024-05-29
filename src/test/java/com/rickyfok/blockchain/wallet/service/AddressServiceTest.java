package com.rickyfok.blockchain.wallet.service;

import com.rickyfok.blockchain.wallet.entity.Address;
import com.rickyfok.blockchain.wallet.repository.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private List<String> mixedAddressList;
    private List<Address> expectedAddressList;

    @BeforeEach
    public void setUp() {
        mixedAddressList = List.of("address1", "", "address2", "", "address3");
        expectedAddressList = List.of(
                new Address(1L, "address1"),
                new Address(2L, "address2"),
                new Address(3L, "address3")
        );
    }

    @Test
    public void saveAddressListStream_withMixedAddresses_returnsSavedAddresses() throws Exception {
        // Arrange
        when(addressRepository.saveAll(anyList())).thenReturn(expectedAddressList);

        // Act
        List<Address> savedAddresses = addressService.saveAddressListStream(mixedAddressList);

        // Assert
        assertNotNull(savedAddresses);
        assertEquals(3, savedAddresses.size());
        assertEquals(expectedAddressList, savedAddresses);
    }

    @Test
    public void saveAddressListStream_withEmptyList_returnsEmptyList() throws Exception {
        // Arrange
        List<String> emptyList = List.of();

        // Act
        List<Address> savedAddresses = addressService.saveAddressListStream(emptyList);

        // Assert
        assertNotNull(savedAddresses);
        assertEquals(0, savedAddresses.size());
    }

    @Test
    public void saveAddressListStream_withSingleNonEmptyAddress_returnsSingleSavedAddress() throws Exception {
        // Arrange
        List<String> singleAddressList = List.of("address1");
        List<Address> singleExpectedAddressList = List.of(new Address(1L, "address1"));
        when(addressRepository.saveAll(anyList())).thenReturn(singleExpectedAddressList);

        // Act
        List<Address> savedAddresses = addressService.saveAddressListStream(singleAddressList);

        // Assert
        assertNotNull(savedAddresses);
        assertEquals(1, savedAddresses.size());
        assertEquals(singleExpectedAddressList, savedAddresses);
    }

    @Test
    public void saveAddressListStream_withSingleEmptyAddress_returnsEmptyList() throws Exception {
        // Arrange
        List<String> singleEmptyAddressList = List.of("");

        // Act
        List<Address> savedAddresses = addressService.saveAddressListStream(singleEmptyAddressList);

        // Assert
        assertNotNull(savedAddresses);
        assertEquals(0, savedAddresses.size());
    }

    @Test
    public void saveAddressListStream_withNullList_returnsNull() throws Exception {
        // Arrange
        List<String> nullList = null;

        // Act
        List<Address> savedAddresses = addressService.saveAddressListStream(nullList);

        // Assert
        assertNull(savedAddresses);
    }
}