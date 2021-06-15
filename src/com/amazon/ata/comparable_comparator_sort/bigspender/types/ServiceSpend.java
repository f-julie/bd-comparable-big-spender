package com.amazon.ata.comparable_comparator_sort.bigspender.types;

/**
 * Represents the amount spent on a single AWS service.
 * Must be associated with a Customer who needs to pay the bill.
 */
public class ServiceSpend {
    private ServiceType serviceName;
    private long spend;

    /**
     * Constructor taking all parameters.
     * @param serviceName The service the customer used.
     * @param spend The amount, in cents, that the customer consumed.
     */
    public ServiceSpend(ServiceType serviceName, long spend) {
        this.serviceName = serviceName;
        this.spend = spend;
    }

    public ServiceType getServiceName() {
        return serviceName;
    }

    public long getSpend() {
        return spend;
    }

    @Override
    public String toString() {
        return "ServiceSpend{" +
            "serviceName=" + serviceName +
            ", spend=" + spend +
            '}';
    }
}
