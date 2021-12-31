package com.medlink.sms.model.token;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
public class ResponseAccessToken {
    @NotBlank
    private String access_token;
    private String expiresIn;
    private Integer tokenType;
    private String scope;
}