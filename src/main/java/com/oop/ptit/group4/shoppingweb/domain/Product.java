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
    @SequenceGenerator(name = "product_id_seq", sequenceName = "product_id_seq", initialValue = 51, allocationSize = 1)
    private Long id;

    @Column(name = "product_title", nullable = false)
    private String productTitle;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "product_color", nullable = false)
    private String productColor;

    @Column(name = "cpu")
    private String cpu;

    @Column(name = "ram")
    private String ram;

    @Column(name = "hard_disk")
    private String hardDisk;

    @Column(name = "card")
    private String card;

    @Column(name = "screen")
    private String screen;

    @Column(name = "connection_port")
    private String connectionPort;

    @Column(name = "webcam")
    private String webCam;

    @Column(name = "battery", nullable = false)
    private String battery;

    @Column(name = "filename")
    private String filename;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "weight")
    private String weight;

    @Column(name = "operating_system", nullable = false)
    private String operatingSystem;
}
