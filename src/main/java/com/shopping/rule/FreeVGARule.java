package com.shopping.rule;

import com.shopping.models.Item;
import com.shopping.models.Rule;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class FreeVGARule implements Rule {

    private final String  VGA_ADAPTER   = "vga";
    private final Integer GROUP_SIZE_MACBOOK = 1;
    private final String  MACBOOK_PRO   = "mbp";

    @Override
    public boolean isMatch(List<Item> items) {
        return filterMacbookItens(items).size() >= GROUP_SIZE_MACBOOK;
    }

    @Override
    public BigDecimal execute(List<Item> items) {
        BigDecimal totals = items.stream()
                .map(Item::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        List<Item> macBooks = filterMacbookItens(items);

        List<Item> vgas = filterVGAItens(items);

        if(vgas.size() > 0 && macBooks.size() > 0) {

            int vgaItemsToPrice = 0;

            if(vgas.size() <= macBooks.size()){
                vgaItemsToPrice = macBooks.size() - (macBooks.size() - vgas.size());
            }else{
                vgaItemsToPrice = vgas.size() - (vgas.size() - macBooks.size());
            }

            BigDecimal vgaPrice = vgas.get(0).getPrice();

            return (vgaItemsToPrice < 0 ? BigDecimal.ZERO :
                    BigDecimal.valueOf(vgaItemsToPrice).multiply(vgaPrice));

        }else{
            return BigDecimal.ZERO;
        }
    }

    private List<Item> filterMacbookItens(List<Item> itens) {
        return itens.stream()
                .filter(item -> item.getSku().equals(MACBOOK_PRO))
                .collect(Collectors.toList());
    }

    private List<Item> filterVGAItens(List<Item> itens) {
        return itens.stream()
                .filter(item -> item.getSku().equals(VGA_ADAPTER))
                .collect(Collectors.toList());
    }
}
