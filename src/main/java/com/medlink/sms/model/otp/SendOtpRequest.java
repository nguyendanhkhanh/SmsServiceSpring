package com.medlink.sms.model.otp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class SendOtpRequest {
    @NotBlank
    private String phoneNo;
    @NotBlank
    private String messageType;
    @NotBlank
    private Integer branchId;
    private int accountId;

}
