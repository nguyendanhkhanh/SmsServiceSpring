package com.medlink.sms.model.otp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Data
public class CheckOtpRequest {
    @NotBlank
    private String phoneNo;
    @NotBlank
    private Integer otp;
    @NotBlank
    private Integer branchId;
}