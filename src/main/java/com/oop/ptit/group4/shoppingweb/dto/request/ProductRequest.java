package com.oop.ptit.group4.shoppingweb.dto.request;

import com.oop.ptit.group4.shoppingweb.constants.ErrorMessage;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ToString
public class ProductRequest {
    private Long id;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String productTitle;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String brand;

//    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
//    @Length(max = 255)
//    private String country;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String productColor;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String battery;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String cpu;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String ram;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String hardDisk;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String card;

//    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
//    @Length(max = 255)
//    private String screen;
//
//    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
//    @Length(max = 255)
//    private String connectionPort;
//
//    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
//    @Length(max = 255)
//    private String webCam;
//
//    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
//    @Length(max = 255)
//    private String battery;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String filename;

//    @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
//    @Length(max = 255)
//    private String weight;

    @NotBlank(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Length(max = 255)
    private String operatingSystem;

    @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Min(value = 1, message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private BigDecimal price;

    @NotNull(message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    @Min(value = 1, message = ErrorMessage.FILL_IN_THE_INPUT_FIELD)
    private int stock;
}
