package com.medlink.sms.model.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseModel implements Serializable {
    private static final long serialVersionUID = -3277553494580760352L;
    private HttpStatus responseStatus;
    private String description;
    private Object data;

    public ResponseModel(Exception e) {
        BaseModel error = new BaseModel();
        if (e.getClass().getSimpleName().equals("ResourceNotFoundException")) {
            this.responseStatus = HttpStatus.NOT_FOUND;
            error.setStatusCode(HttpStatus.NOT_FOUND.value());
        } else if (e.getClass().getSimpleName().equals("ForbiddenException")) {
            this.responseStatus = HttpStatus.FORBIDDEN;
            error.setStatusCode(HttpStatus.FORBIDDEN.value());
        } else if (e.getClass().getSimpleName().equals("BadRequestException")) {
            this.responseStatus = HttpStatus.BAD_REQUEST;
            error.setStatusCode(HttpStatus.BAD_REQUEST.value());
        } else {
            this.responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            error.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        error.setMessage(e.getMessage());
        this.description = e.getMessage();
        this.data = error;
    }

}