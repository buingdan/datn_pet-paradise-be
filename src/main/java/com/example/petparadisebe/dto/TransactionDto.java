package com.example.petparadisebe.dto;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Order;
import com.example.petparadisebe.Entities.Promotion;
import com.example.petparadisebe.Entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TransactionDto implements Serializable {
    private Long id;
    private String billNo;
    private String transNo;
    private String bankCode;
    private String cardType;
    private Integer amount;
    private String currency;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_date")
    private LocalDateTime createDate;

    private String orderInfor;
    private String status;
    private String paymentTime;

    private Order order;
    private User user;
}
