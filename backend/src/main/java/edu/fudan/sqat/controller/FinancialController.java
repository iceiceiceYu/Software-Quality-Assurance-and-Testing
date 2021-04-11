package edu.fudan.sqat.controller;

import edu.fudan.sqat.controller.request.PurchaseRequest;
import edu.fudan.sqat.domain.FinancialProduct;
import edu.fudan.sqat.domain.Purchase;
import edu.fudan.sqat.service.FinancialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class FinancialController {
    private final FinancialService financialService;

    @Autowired
    public FinancialController(FinancialService financialService) {
        this.financialService = financialService;
    }

    @PostMapping("/financial/increase")
    public ResponseEntity<String> increase(@RequestBody String username) {
        return ResponseEntity.ok(financialService.increase());
    }

    @PostMapping("/financial/purchaseInfo")
    public ResponseEntity<List<Purchase>> purchaseInfo(@RequestBody String IDCode) {
        return ResponseEntity.ok(financialService.purchaseInfo(IDCode));
    }

    @PostMapping("/financial/accountLevel")
    public ResponseEntity<Integer> accountLevel(@RequestBody String IDCode) {
        return ResponseEntity.ok(financialService.accountLevel(IDCode));
    }

    @PostMapping("/financial/allInfo")
    public ResponseEntity<List<FinancialProduct>> allInfo(@RequestBody String username) {
        return ResponseEntity.ok(financialService.allInfo());
    }

    @PostMapping("/financial/purchaseProduct")
    public ResponseEntity<String> purchaseProduct(@RequestBody PurchaseRequest purchaseRequest) {
        return ResponseEntity.ok(financialService.purchaseProduct(
                purchaseRequest.getIDCode(),
                purchaseRequest.getName(),
                purchaseRequest.getType(),
                purchaseRequest.getStockAmount(),
                purchaseRequest.getDate()));
    }
}
