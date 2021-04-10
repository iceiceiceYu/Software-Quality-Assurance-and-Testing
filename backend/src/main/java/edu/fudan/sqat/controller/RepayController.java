package edu.fudan.sqat.controller;

import edu.fudan.sqat.controller.request.RepaymentRequest;
import edu.fudan.sqat.domain.Loan;
import edu.fudan.sqat.service.RepayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
public class RepayController {
    private RepayService repayService;

    @Autowired
    public RepayController(RepayService repayService) {
        this.repayService = repayService;
    }

    @PostMapping("/repay/identification")
    public ResponseEntity<Boolean> identification(@RequestBody String IDCode) {
        return ResponseEntity.ok(repayService.identification(IDCode));
    }

    @PostMapping("/repay/loanInfo")
    public ResponseEntity<List<Loan>> loanInfo(@RequestBody Long id) {
        return ResponseEntity.ok(repayService.loanInfo(id));
    }

    @PostMapping("/repay/repayment")
    public ResponseEntity<String> repayment(@RequestBody RepaymentRequest repaymentRequest) {

    }

    @PostMapping("/repay/autoRepayment")
    public ResponseEntity<> autoRepayment(@RequestBody Boolean isAuto){

    }
}
