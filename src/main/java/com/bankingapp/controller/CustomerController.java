package com.bankingapp.controller;

import com.bankingapp.dto.AddressDto;
import com.bankingapp.dto.CustomerDto;
import com.bankingapp.model.Address;
import com.bankingapp.model.Customer;

import com.bankingapp.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto.CustomerResponse> createCustomer(@Valid @RequestBody CustomerDto.CustomerRequest request) {
        Customer customerEntity = mapToCustomerEntity(request);
        Customer savedCustomer = customerService.saveCustomer(customerEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToCustomerResponse(savedCustomer));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto.CustomerResponse> getCustomerById(@PathVariable Long customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        return ResponseEntity.ok(mapToCustomerResponse(customer));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto.CustomerResponse> updateCustomer(
            @PathVariable Long customerId,
            @Valid @RequestBody CustomerDto.CustomerRequest request) {
        Customer customerEntity = mapToCustomerEntity(request);
        Customer updatedCustomer = customerService.updateCustomer(customerId, customerEntity);
        return ResponseEntity.ok(mapToCustomerResponse(updatedCustomer));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @PostMapping("/{customerId}/address")
    public ResponseEntity<CustomerDto.CustomerResponse> addCustomerAddress(
            @PathVariable Long customerId,
            @Valid @RequestBody AddressDto.AddressRequest request) {
        Address addressEntity = mapToAddressEntity(request);
        Customer updatedCustomer = customerService.addCustomerAddress(customerId, addressEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToCustomerResponse(updatedCustomer));
    }

    @PutMapping("/{customerId}/address")
    public ResponseEntity<CustomerDto.CustomerResponse> updateCustomerAddress(
            @PathVariable Long customerId,
            @Valid @RequestBody AddressDto.AddressRequest request) {
        Address addressEntity = mapToAddressEntity(request);
        Customer updatedCustomer = customerService.updateCustomerAddress(customerId, addressEntity);
        return ResponseEntity.ok(mapToCustomerResponse(updatedCustomer));
    }

    // ==========================================
    // Mapping Helper Methods (Model <-> DTO)
    // ==========================================

    private CustomerDto.CustomerResponse mapToCustomerResponse(Customer customer) {
        if (customer == null) return null;

        AddressDto.AddressResponse addressResponse = mapToAddressResponse(customer.getAddress());

        return new CustomerDto.CustomerResponse(
                customer.getCustomerId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getDateOfBirth(),
                customer.getGender(),
                addressResponse,
                customer.getCreatedDate(),
                customer.getUpdatedDate()
        );
    }

    private AddressDto.AddressResponse mapToAddressResponse(Address address) {
        if (address == null) return null;

        return new AddressDto.AddressResponse(
                address.getAddressId(),
                address.getAddress(),
                address.getCity(),
                address.getState(),
                address.getZip(),
                address.getCountry(),
                address.getCreatedDate(),
                address.getUpdatedDate()
        );
    }

    private Customer mapToCustomerEntity(CustomerDto.CustomerRequest request) {
        Customer customer = new Customer();
        customer.setFirstName(request.firstName());
        customer.setLastName(request.lastName());
        customer.setEmail(request.email());
        customer.setPassword(request.password());
        customer.setPhoneNumber(request.phoneNumber());
        customer.setDateOfBirth(request.dateOfBirth());
        customer.setGender(request.gender());

        if (request.address() != null) {
            customer.setAddress(mapToAddressEntity(request.address()));
        }

        return customer;
    }

    private Address mapToAddressEntity(AddressDto.AddressRequest request) {
        Address address = new Address();
        address.setAddress(request.address());
        address.setCity(request.city());
        address.setState(request.state());
        address.setZip(request.zip());
        address.setCountry(request.country());
        return address;
    }
}
