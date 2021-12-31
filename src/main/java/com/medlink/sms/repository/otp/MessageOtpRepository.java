package com.medlink.sms.repository.otp;

import com.medlink.sms.entity.otp.SmsMessageOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public interface MessageOtpRepository extends JpaRepository<SmsMessageOtp, Integer> {

    SmsMessageOtp getFirstByPhoneNoAndBranchIdOrderByExpireDateDesc(String phoneNo, Integer branchId);
    SmsMessageOtp findByPhoneNoAndBranchIdAndExpireDateAfter(String phoneNo, Integer branchId, Timestamp checkTime);
}
