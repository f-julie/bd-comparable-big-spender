package com.amazon.ata.comparable_comparator_sort.bigspender.types;

import java.util.Comparator;
public class CustomerServiceSpendComparator  implements Comparator<CustomerServiceSpend> {
    @Override
    public int compare(CustomerServiceSpend css1, CustomerServiceSpend css2) {
        if (css1.equals(css2)) {
            return 0;
        } else {
            if (css1.getServiceSpend().getSpend() != css2.getServiceSpend().getSpend()) {
                return (int) (css1.getServiceSpend().getSpend() - css2.getServiceSpend().getSpend());
            } else {
                // service spends are equal
                return css1.getServiceSpend().getServiceName().compareTo(css2.getServiceSpend().getServiceName());
            }
        }
    }
}
