package com.cloudcart.service.interfaces;

import com.cloudcart.dto.CustomerRequest;
import com.cloudcart.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {
    List<CustomerResponse> getAllCustomers();
    CustomerResponse getCustomerById(Long id);
    CustomerResponse createCustomer(CustomerRequest request);
    CustomerResponse updateCustomer(Long id, CustomerRequest request);
}
