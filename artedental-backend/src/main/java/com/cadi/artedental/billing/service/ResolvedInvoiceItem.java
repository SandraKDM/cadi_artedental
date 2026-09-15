package com.cadi.artedental.billing.service;

import java.math.BigDecimal;

public record ResolvedInvoiceItem(

    String description,

    BigDecimal amount

) {
}