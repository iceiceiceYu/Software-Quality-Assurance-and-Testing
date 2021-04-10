package com.softwaretesting.demo.controller;

import com.softwaretesting.demo.controller.request.PurchaseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class PurchaseController {

    @PostMapping("/jianchashifoukeyimai")
    public ResponseEntity<String> checkPurchaseQualifiation(PurchaseRequest request) throws IOException {

        String responseMsg = "未交罚金，请先去交";


        return ResponseEntity.ok(responseMsg);
    }

    @PostMapping("/purchase")
    public ResponseEntity<Boolean> purchase(PurchaseRequest request) throws IOException {




        return ResponseEntity.ok(Boolean.TRUE);
    }

}
