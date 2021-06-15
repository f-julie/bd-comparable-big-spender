package com.amazon.ata.comparable_comparator_sort.bigspender.dao;

import com.amazon.ata.comparable_comparator_sort.bigspender.types.Customer;
import com.amazon.ata.comparable_comparator_sort.bigspender.types.CustomerServiceSpend;
import com.amazon.ata.comparable_comparator_sort.bigspender.types.CustomerTotalSpend;
import com.amazon.ata.comparable_comparator_sort.bigspender.types.ServiceSpend;

import com.google.common.annotations.VisibleForTesting;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.amazon.ata.comparable_comparator_sort.bigspender.types.ServiceType.DYNAMODB;
import static com.amazon.ata.comparable_comparator_sort.bigspender.types.ServiceType.EC2;
import static com.amazon.ata.comparable_comparator_sort.bigspender.types.ServiceType.S3;

/**
 * This class provides AWS customer spending totals and itemized list of service spends
 * that the participants need to complete the  activity for Comparable, Comparator, sort.
 */
public class AwsServiceInvoiceDao {
    private Map<String, List<ServiceSpend>> charges;
    private Map<String, Customer> customerData;

    private List<CustomerTotalSpend> allServiceSpends;
    private List<CustomerServiceSpend> highestServiceSpendsForEachCustomer;

    /**
     * Constructor that calculates the invoices and highest spends.
     */
    public AwsServiceInvoiceDao() {
        this.charges = new HashMap<>();
        charges.put("nedflix", Arrays.asList(new ServiceSpend(S3, 50000L),
            new ServiceSpend(DYNAMODB, 5000L),
            new ServiceSpend(EC2, 20000L)));
        charges.put("godiddy", Arrays.asList(new ServiceSpend(EC2, 40000L),
            new ServiceSpend(S3, 5000L)));
        charges.put("backup", Arrays.asList(new ServiceSpend(S3, 70000L)));

        // Load from file?
        this.customerData = new HashMap<>();
        customerData.put("nedflix", new Customer("nedflix", LocalDate.now()));
        customerData.put("godiddy", new Customer("godiddy", LocalDate.now()));
        customerData.put("backup", new Customer("backup", LocalDate.now()));

        allServiceSpends = calculateAllServiceSpends();
        highestServiceSpendsForEachCustomer = calculateHighestSpends();
    }

    @VisibleForTesting
    public Map<String, List<ServiceSpend>> getCharges() {
        return charges;
    }

    @VisibleForTesting
    public Map<String, Customer> getCustomerData() {
        return customerData;
    }

    /**
     * Returns a list containing each customer's highest service spend.
     * @return {@code List<CustomerServiceSpend>} with one entry for each customer,
     *         containing the service spend for the single highest service spend
     *         for that customer
     */
    public List<CustomerServiceSpend> getHighestServiceSpendsForEachCustomer() {
        return new ArrayList<>(highestServiceSpendsForEachCustomer);
    }

    /**
     * Returns a list of all total spends for every customer.
     * @return a {@code List<CustomerTotalSpend>} with one entry for each customer,
     *         containing their total spend (and all individual service spends contained
     *         within)
     */
    public List<CustomerTotalSpend> getAllServiceSpends() {
        return new ArrayList<>(allServiceSpends);
    }

    private List<CustomerTotalSpend> calculateAllServiceSpends() {
        List<CustomerTotalSpend> allCustomersSpends = new ArrayList<>();

        for (Map.Entry<String, List<ServiceSpend>> charge : charges.entrySet()) {
            Customer customer = getCustomerForName(charge.getKey());

            List<ServiceSpend> serviceCharges = charge.getValue();
            CustomerTotalSpend customerTotalSpend = new CustomerTotalSpend(customer, serviceCharges);

            allCustomersSpends.add(customerTotalSpend);
        }
        return allCustomersSpends;
    }

    private List<CustomerServiceSpend> calculateHighestSpends() {
        List<CustomerServiceSpend> highestSpendPerCustomer = new ArrayList<>();

        for (Map.Entry<String, List<ServiceSpend>> charge : charges.entrySet()) {
            // Find the customer
            String customerName = charge.getKey();
            Customer customer = getCustomerForName(customerName);

            List<ServiceSpend> serviceCharges = charge.getValue();

            ServiceSpend maxSpend = serviceCharges.stream()
                .max(Comparator.comparingLong(ServiceSpend::getSpend))
                .orElseThrow(() -> new IllegalStateException("No max found for customer " + customerName));

            highestSpendPerCustomer.add(new CustomerServiceSpend(customer, maxSpend));
        }
        return highestSpendPerCustomer;
    }

    private Customer getCustomerForName(String name) {
        if (!customerData.containsKey(name)) {
            throw new IllegalArgumentException("No such customer: " + name + "!");
        }
        return customerData.get(name);
    }
}
