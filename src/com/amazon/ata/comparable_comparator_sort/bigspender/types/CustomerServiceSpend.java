package com.amazon.ata.comparable_comparator_sort.bigspender.types;

import java.util.Objects;

/**
 * Represents both a Customer and the details of one AWS service spend.
 */
public class CustomerServiceSpend implements Comparable<CustomerServiceSpend> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerServiceSpend that = (CustomerServiceSpend) o;
        return Objects.equals(this.customer, that.customer) && Objects.equals(this.serviceSpend, that.serviceSpend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, serviceSpend);
    }

    @Override
    public int compareTo(CustomerServiceSpend that) {
        if (this.equals(that)) {
            return 0;
        } else {
            if (!this.customer.equals(that.getCustomer())) {
                return this.customer.compareTo(that.getCustomer());
            } else {
                return (this.serviceSpend.getServiceName().compareTo(that.getServiceSpend().getServiceName()));
            }
        }
    }
}
