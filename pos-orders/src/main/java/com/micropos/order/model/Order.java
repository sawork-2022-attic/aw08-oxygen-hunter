package com.micropos.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Accessors(fluent = true, chain = true)
public class Order {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private Integer id;

    @Setter
    @Getter
    private String time;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "order_items",
            joinColumns = @JoinColumn(
                    name = "order_id",
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
}
