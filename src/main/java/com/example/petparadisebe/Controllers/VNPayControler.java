package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Entities.Transaction;
import com.example.petparadisebe.Repositories.TransactionRepository;
import com.example.petparadisebe.Repositories.UserRepository;
import com.example.petparadisebe.Services.VNPayService;
import com.example.petparadisebe.dto.TransactionDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/vnpay")
public class VNPayControler {
    @Autowired
    private VNPayService vnPayService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/submitOrder")
    //POST http://localhost:8090/api/v1/vnpay/submitOrder
    public String submidOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);
        return vnpayUrl;
    }

    @GetMapping("/vnpay-payment")
    //GET http://localhost:8090/api/v1/vnpay/vnpay-payment
    public String GetMapping(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);


        Transaction transaction = new Transaction();
        //Giống mã gửi sang VNPAY khi gửi yêu cầu thanh toán.  Ví dụ: 23554
        transaction.setBillNo(request.getParameter("vnp_TxnRef"));
        //Mã giao dịch ghi nhận tại hệ thống VNPAY. Ví dụ: 20170829153052
        transaction.setTransNo(request.getParameter("vnp_TransactionNo"));
        //Mã Ngân hàng thanh toán. Ví dụ: NCB
        transaction.setBankCode(request.getParameter("vnp_BankCode"));
        //Loại tài khoản/thẻ khách hàng sử dụng:ATM,QRCODE
        transaction.setCardType(request.getParameter("vnp_CardType"));
        //Số tiền thanh toán. VNPAY phản hồi số tiền nhân thêm 100 lần.
        transaction.setAmount(Integer.parseInt(request.getParameter("vnp_Amount")));
        transaction.setCurrency("VND");
        //Mô tả chuyển khoản của khách hàng
        transaction.setOrderInfor(request.getParameter("vnp_OrderInfo"));
        //Thời điểm giao dịch
        transaction.setPaymentTime(request.getParameter("vnp_PayDate"));
//        transaction.getOrder().setId(dto.getOrder().getId());
//        transaction.getUser().setId(dto.getUser().getId());
        if(paymentStatus == 1) {
            transaction.setStatus("Đã thanh toán");
            transactionRepository.save(transaction);
            model.addAttribute("message", "Giao dịch thành công");
        } else if(paymentStatus == 0) {
            transaction.setStatus("Chưa thanh toán");
            transactionRepository.save(transaction);
            model.addAttribute("message", "Giao dịch thất bại");
        }else{
            model.addAttribute("message", "Lỗi !!! Mã Secure Hash không hợp lệ.");
        }

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";

    }
}
