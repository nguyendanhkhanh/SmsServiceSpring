package com.medlink.sms.service.otp;

import com.google.gson.Gson;
import com.medlink.sms.entity.otp.SmsBranchName;
import com.medlink.sms.entity.otp.SmsMessageOtp;
import com.medlink.sms.model.otp.CheckOtpRequest;
import com.medlink.sms.model.otp.SendOtpRequest;
import com.medlink.sms.model.response.BaseModel;
import com.medlink.sms.model.response.ResponseModel;
import com.medlink.sms.repository.otp.BranchNameRepository;
import com.medlink.sms.repository.otp.MessageOtpRepository;
import com.medlink.sms.service.token.AccessTokenService;
import com.medlink.sms.utils.Constant;
import com.medlink.sms.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
public class OtpService {
    private static final Logger log = LogManager.getLogger(OtpService.class);
    private RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<String> result;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    MessageOtpRepository messageOtpRepository;

    @Autowired
    BranchNameRepository branchNameRepository;

    @Value("${fpt_push_brand_name_otp_url}")
    private String pushBranchNameOtpUrl;


    @Transactional
    public ResponseModel sendOtpFpt(SendOtpRequest sendOtpRequest) {

        ResponseModel responseModel = new ResponseModel();
        String messageResponse = "Send OTP success";
        String messageResponseError = "Send OTP fail";
        BaseModel success = new BaseModel(HttpStatus.OK.value(), messageResponse);
        BaseModel error = new BaseModel(HttpStatus.BAD_REQUEST.value(), messageResponseError);

        SmsBranchName smsBranchName = branchNameRepository.getByBranchId(sendOtpRequest.getBranchId());
        if (smsBranchName == null) {
            String messageResponseErrorBrandId = "brand_id is not exist!";
            responseModel.setData(error);
            responseModel.setDescription(messageResponseErrorBrandId);
            responseModel.setResponseStatus(HttpStatus.BAD_REQUEST);
            return responseModel;
        }

        SmsMessageOtp oldSmsMessageOtp = messageOtpRepository.getFirstByPhoneNoAndBranchIdOrderByExpireDateDesc(sendOtpRequest.getPhoneNo(), smsBranchName.getBranchId());
        if (oldSmsMessageOtp != null && oldSmsMessageOtp.getExpireDate() != null) {
            long diff = (new Date().getTime()) - oldSmsMessageOtp.getExpireDate().getTime();
            if (diff / 1000 <= Constant.EXPIRED_OTP.EXPIRED_TIME_OTP) {
                Timestamp timestampNow = new Timestamp(new Date().getTime());
                oldSmsMessageOtp.setExpireDate(timestampNow);
                messageOtpRepository.save(oldSmsMessageOtp);
            }
        }

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 3);
        Date expireDateTime = now.getTime();
        Timestamp expireTime = new Timestamp(expireDateTime.getTime());

        Random generator = new Random();
        int otp = generator.nextInt((999999 - 100000) + 1) + 100000;

        SmsMessageOtp smsMessageOtp = new SmsMessageOtp();
        smsMessageOtp.setOtpValue(otp);
        smsMessageOtp.setMessageType(sendOtpRequest.getMessageType().equals(Constant.MESSAGE_TYPE.MESSAGE_OTP_REGISTER) ?
                Constant.MESSAGE_TYPE.MESSAGE_OTP_REGISTER : Constant.MESSAGE_TYPE.MESSAGE_OTP_FORGOT_PASSWORD);
        smsMessageOtp.setPhoneNo(sendOtpRequest.getPhoneNo());
        smsMessageOtp.setExpireDate(expireTime);
        smsMessageOtp.setAccountId(sendOtpRequest.getAccountId());
        smsMessageOtp.setBranchId(sendOtpRequest.getBranchId());
        messageOtpRepository.save(smsMessageOtp);

        String message = "Mã OTP của bạn là " + sendOtpRequest.getPhoneNo();
        String messageBase64 = Base64.getEncoder().encodeToString(message.getBytes());

        JSONObject map = new JSONObject();
        map.put("access_token", smsBranchName.getAccessToken());
        map.put("session_id", String.valueOf(smsMessageOtp.getMessageOtpId()));
        map.put("BrandName", smsBranchName.getBranchName());
        map.put("Phone", sendOtpRequest.getPhoneNo());
        map.put("Message", messageBase64);
        map.put("RequestId", "123213");
        String url = pushBranchNameOtpUrl;

        try {
            log.info(JsonUtils.convertObjectToJson(map));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(map, headers);

            result = restTemplate.postForEntity(url, entity, String.class);
            log.info(result.getStatusCode().value());

            responseModel.setData(success);
            responseModel.setDescription(messageResponse);
            responseModel.setResponseStatus(HttpStatus.OK);
        } catch (HttpStatusCodeException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.info(e.getMessage());
            if (e.getRawStatusCode() == 401) {
                String newAccessToken = accessTokenService.requestAccessToken(smsBranchName);
                map.put("access_token", newAccessToken);
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(map, headers);
                result = restTemplate.postForEntity(url, entity, String.class);
                System.out.println(result);
                smsBranchName.setAccessToken(newAccessToken);
                branchNameRepository.save(smsBranchName);
                responseModel.setData(success);
                responseModel.setDescription(messageResponse);
                responseModel.setResponseStatus(HttpStatus.OK);
            } else {
                responseModel.setData(error);
                responseModel.setDescription(messageResponseError);
                responseModel.setResponseStatus(e.getRawStatusCode() == 401 ? HttpStatus.UNAUTHORIZED : HttpStatus.BAD_REQUEST);
            }
        }
        return responseModel;
    }

    public ResponseModel checkOtpFpt(CheckOtpRequest checkOtpRequest) {
        ResponseModel responseModel = new ResponseModel();

        String messageResponse = "OTP is correct";
        String messageResponseError = "OTP is incorrect";
        BaseModel success = new BaseModel(HttpStatus.OK.value(), messageResponse);
        BaseModel error = new BaseModel(HttpStatus.BAD_REQUEST.value(), messageResponseError);

        Timestamp now = new Timestamp(new Date().getTime());

        SmsMessageOtp oldSmsMessageOtp = messageOtpRepository.findByPhoneNoAndBranchIdAndExpireDateAfter(checkOtpRequest.getPhoneNo(), checkOtpRequest.getBranchId(), now);
        System.out.println(oldSmsMessageOtp);
        if (oldSmsMessageOtp != null && oldSmsMessageOtp.getOtpValue().equals(checkOtpRequest.getOtp())) {
            responseModel.setData(success);
            responseModel.setDescription(messageResponse);
            responseModel.setResponseStatus(HttpStatus.OK);
        } else {
            responseModel.setData(error);
            responseModel.setDescription(messageResponseError);
            responseModel.setResponseStatus(HttpStatus.UNAUTHORIZED);
        }

        return responseModel;
    }

}
