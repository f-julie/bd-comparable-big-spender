package com.amazon.ata.comparable_comparator_sort.bigspender.types;

/**
 * Represents both a Customer and the details of one AWS service spend.
 */
public class CustomerServiceSpend {
    private Customer customer;
    private ServiceSpend serviceSpend;

    /**
     * Constructor taking a customer and a service spend.
     * @param customer the customer.
     * @param serviceSpend The service spend.
     */
    public CustomerServiceSpend(Customer customer, ServiceSpend serviceSpend) {
        this.customer = customer;
        this.serviceSpend = serviceSpend;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ServiceSpend getServiceSpend() {
        return serviceSpend;
    }

    @Override
    public String toString() {
        return "CustomerServiceSpend{" +
               "customer=" + customer +
               ", serviceSpend=" + serviceSpend +
               '}';
    }
}
