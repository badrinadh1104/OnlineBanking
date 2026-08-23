package com.bankingapp.service;

import com.bankingapp.model.Address;
import com.bankingapp.model.Customer;

public interface ICustomerService {
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(Long customerId, Customer customerDetails);
    void deleteCustomer(Long customerId);
    Customer addCustomerAddress(Long customerId, Address address);
    Customer updateCustomerAddress(Long customerId, Address updatedAddress);
    Customer getCustomerById(Long customerId);
}
