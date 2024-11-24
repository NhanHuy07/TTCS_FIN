package com.oop.ptit.group4.shoppingweb.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_id_seq")
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", initialValue = 109, allocationSize = 1)
    private Long id;

    @Column(name = "product_title", nullable = false)
    private String productTitle;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "product_color", nullable = false)
    private String productColor;

    @Column(name = "battery_capacity", nullable = false)
    private String batteryCapacity;

    @Column(name = "storage_capacity", nullable = false)
    private String storageCapacity;

    @Column(name = "ram_capacity", nullable = false)
    private String ramCapacity;

    @Column(name = "description")
    private String description;

    @Column(name = "filename")
    private String filename;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "operating_system", nullable = false)
    private String operatingSystem;

    @Column(name = "model", nullable = false)
    private String model;
}
