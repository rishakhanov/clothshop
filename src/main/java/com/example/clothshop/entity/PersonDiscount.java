package com.example.clothshop.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "person_discount")
public class PersonDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Person person;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    private Discount discount;

    public Long getId() {
        return id;
    }


    public Person getPerson() {
        return person;
    }

    public Discount getDiscount() {
        return discount;
    }
}
