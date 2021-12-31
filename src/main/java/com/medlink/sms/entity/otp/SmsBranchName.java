package com.medlink.sms.entity.otp;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "sms_branch_name", schema = "medlink_sms")
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SmsBranchName {
    @Id
    @Column(name = "branch_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int branchId;
    @Basic
    @Column(name = "branch_name", nullable = false, length = 32)
    private String branchName;
    @Basic
    @Column(name = "client_id", nullable = false, length = 256)
    private String clientId;
    @Basic
    @Column(name = "client_secret", nullable = false, length = 512)
    private String clientSecret;
    @Basic
    @Column(name = "access_token", nullable = false, length = 512)
    private String accessToken;
    @Basic
    @Column(name = "expire_date", nullable = false)
    private Timestamp expireDate;

}
