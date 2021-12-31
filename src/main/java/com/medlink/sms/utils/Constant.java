package com.medlink.sms.utils;

public interface Constant {
    interface MESSAGE_BRANCH_NAME {
        String BRAND_NAME = "MEDLINK";
        String BRAND_NAME_DEMO = "FTI";
    }

    interface  MESSAGE_TYPE {
        String MESSAGE_OTP_REGISTER = "REGISTER";
        String MESSAGE_OTP_FORGOT_PASSWORD = "FORGOT_PASSWORD";
        String MESSAGE_MARKETING = "MARKETING";
    }

    interface EXPIRED_OTP {
        long EXPIRED_TIME_OTP = 180; // second
    }
}
