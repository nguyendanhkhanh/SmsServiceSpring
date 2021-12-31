package com.medlink.sms.repository.otp;

import com.medlink.sms.entity.otp.SmsBranchName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchNameRepository extends JpaRepository<SmsBranchName, Integer> {
    SmsBranchName getByBranchId(Integer branch_id);
}
