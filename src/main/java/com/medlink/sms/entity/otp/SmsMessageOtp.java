package com.medlink.sms.entity.otp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sms_message_otp", schema = "medlink_sms")
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SmsMessageOtp {
    @Id
    @Column(name = "message_otp_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageOtpId;
    @Basic
    @Column(name = "otp_value", nullable = false)
    private Integer otpValue;
    @Basic
    @Column(name = "message_type", nullable = true, length = 64)
    private String messageType;
    @Basic
    @Column(name = "phone_no", nullable = false, length = 32)
    private String phoneNo;
    @Basic
    @Column(name = "expire_date", nullable = false)
    private Timestamp expireDate;
    @Basic
    @Column(name = "account_id", nullable = true)
    private Integer accountId;
    @Basic
    @Column(name = "branch_id", nullable = false)
    private Integer branchId;
    @Basic
    @Column(name = "request_id", nullable = true)
    private String requestId;
}
