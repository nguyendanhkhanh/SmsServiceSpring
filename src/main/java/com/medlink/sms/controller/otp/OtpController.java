package com.medlink.sms.controller.otp;

import com.medlink.sms.model.otp.CheckOtpRequest;
import com.medlink.sms.model.otp.SendOtpRequest;
import com.medlink.sms.model.response.ResponseModel;
import com.medlink.sms.service.otp.OtpService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;


@RestController
@RequestMapping("/api/otp")
public class OtpController {
    private static final Logger log = LogManager.getLogger(OtpController.class);

    @Autowired
    private OtpService otpService;

    @PostMapping("/request")
    public ResponseEntity requestOtp(@Valid @RequestBody SendOtpRequest sendOtpRequest) {
        log.info("Request OTP for " + sendOtpRequest.getMessageType() + ": " + sendOtpRequest.getPhoneNo());
        long start = System.currentTimeMillis();
        ResponseModel model = otpService.sendOtpFpt(sendOtpRequest);
        long end = System.currentTimeMillis();
        long diff = end - start;
        log.info("Code = " + model.getResponseStatus() + "," + model.getDescription() + ",time = " + diff);
        return new ResponseEntity(model.getData(), model.getResponseStatus());
    }
    @PostMapping("/check")
    public ResponseEntity checkOtpFpt(@Valid @RequestBody CheckOtpRequest checkOtpRequest) {
        log.info("Check OTP for " + checkOtpRequest.getPhoneNo());
        long start = System.currentTimeMillis();
        ResponseModel model = otpService.checkOtpFpt(checkOtpRequest);
        long end = System.currentTimeMillis();
        long diff = end - start;
        log.info("Code = " + model.getResponseStatus() + "," + model.getDescription() + ",time = " + diff);
        return new ResponseEntity(model.getData(), model.getResponseStatus());
    }
}
