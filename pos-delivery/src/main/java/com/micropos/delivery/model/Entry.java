package com.micropos.delivery.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "delivery_entries")
@Accessors(fluent = true, chain = true)
public class Entry implements Serializable {
    @Id
    @Getter
    @Setter
    private Integer orderId;

    @Column(name="status")
    @Getter
    @Setter
    private String status;
}
