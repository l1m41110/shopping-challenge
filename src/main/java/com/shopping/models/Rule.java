package com.shopping.models;

import java.math.BigDecimal;
import java.util.List;

public interface Rule {

    boolean isMatch(List<Item> items);

    BigDecimal execute(List<Item> items);
}
