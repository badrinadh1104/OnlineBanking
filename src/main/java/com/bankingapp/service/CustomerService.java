package com.bankingapp.service;

import com.bankingapp.exception.CustomerAlreadyExistsException;
import com.bankingapp.exception.ResourceNotFoundException;
import com.bankingapp.model.Address;
import com.bankingapp.model.Customer;
import com.bankingapp.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService implements ICustomerService{

    private final CustomerRepository customerRepository;


    @Override
    public Customer saveCustomer(Customer customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new CustomerAlreadyExistsException(
                    "Customer already exists with email: " + customer.getEmail()
            );
        }
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
    }

    @Override
    public Customer updateCustomer(Long customerId, Customer customerDetails) {
        Customer existingCustomer = getCustomerById(customerId);

        existingCustomer.setFirstName(customerDetails.getFirstName());
        existingCustomer.setLastName(customerDetails.getLastName());
        existingCustomer.setEmail(customerDetails.getEmail());
        existingCustomer.setPassword(customerDetails.getPassword());
        existingCustomer.setPhoneNumber(customerDetails.getPhoneNumber());
        existingCustomer.setDateOfBirth(customerDetails.getDateOfBirth());
        existingCustomer.setGender(customerDetails.getGender());

        return customerRepository.save(existingCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        Customer customer = getCustomerById(customerId);
        customerRepository.delete(customer);
    }

    @Override
    public Customer addCustomerAddress(Long customerId, Address address) {
        Customer customer = getCustomerById(customerId);

        if (customer.getAddress() != null) {
            throw new IllegalStateException("Customer already has an address. Use update address instead.");
        }

        customer.setAddress(address);
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomerAddress(Long customerId, Address updatedAddress) {
        Customer customer = getCustomerById(customerId);
        Address existingAddress = customer.getAddress();

        if (existingAddress == null) {
            // If no address exists, assign the new one
            customer.setAddress(updatedAddress);
        } else {
            // Update fields of the existing address to preserve primary key and lifecycle
            existingAddress.setAddress(updatedAddress.getAddress());
            existingAddress.setCity(updatedAddress.getCity());
            existingAddress.setState(updatedAddress.getState());
            existingAddress.setZip(updatedAddress.getZip());
            existingAddress.setCountry(updatedAddress.getCountry());
        }

        return customerRepository.save(customer);
    }
}
