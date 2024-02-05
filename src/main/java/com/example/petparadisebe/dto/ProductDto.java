package com.example.petparadisebe.dto;

import com.example.petparadisebe.Entities.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class ProductDto implements Serializable {
    private Long id;
    private String name;
    private double price;
    private String image;

    @JsonProperty("is_delete")
    private boolean isDelete;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_date")
    private LocalDateTime createDate;

    @JsonProperty("quantity_in_stock")
    private int quantityInStock;

    private double discount;
    private Category category;

    @JsonIgnore
    private MultipartFile imgFile;
}
