package com.example.petparadisebe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentDto {
    private List list;

    private Integer totalRecord;

    private  Integer currentPage;

}