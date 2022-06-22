package com.micropos.cart.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Accessors(fluent = true, chain = true)
public class Cart implements Serializable {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "cart_items",
            joinColumns = @JoinColumn(
                    name = "cart_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "item_id",
                    referencedColumnName = "id"
            )
    )
    @Setter
    @Getter
    private List<Item> items = new ArrayList<>();

    public boolean addItem(Item item) {
        // items.id can not conflict
        return items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

}
