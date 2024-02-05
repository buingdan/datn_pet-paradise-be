package com.example.petparadisebe.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
@Data
public class CategoryDto implements Serializable {
    private Long id;

    @NotEmpty(message = "Yêu cầu nhập tên danh mục sản phẩm")
    private String name;

    @JsonProperty("is_delete")
    private boolean isDelete;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_date")
    private LocalDateTime createDate;
}
