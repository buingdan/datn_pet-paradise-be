package com.example.petparadisebe.dto;

import com.example.petparadisebe.Entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class OrderDto implements Serializable {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;

    @JsonProperty("is_delete")
    private boolean isDelete;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_date")
    private LocalDateTime createDate;

    private User user;


}
