package com.shopping.service;

import com.shopping.models.Item;
import com.shopping.models.Rule;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Checkout {

    private final List<Rule> pricingRules;
    private final List<Item> items = new ArrayList<Item>();

    public Checkout(List<Rule> pricingRules) {
        this.pricingRules = pricingRules;
    }

    public void scan(Item item){
        items.add(item);
    }

    public BigDecimal total(){
        BigDecimal total = items.stream()
                .map(item -> item.getPrice())
                .reduce(BigDecimal::add)
                .get();

        BigDecimal discount = pricingRules.stream()
                .filter(x -> x.isMatch(items))
                .map(x -> x.execute(items))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        return total.subtract(discount);
    }

}
