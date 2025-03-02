package enums;

import java.util.Arrays;

public enum PaymentMethod {
    VOUCHER("VOUCHER"),
    CASH_ON_DELIVERY("CASH_ON_DELIVERY"),
    BANK_TRANSFER("BANK_TRANSFER");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String method) {
        return Arrays.stream(PaymentMethod.values())
                .anyMatch(paymentMethod -> paymentMethod.getValue().equals(method));
    }
}