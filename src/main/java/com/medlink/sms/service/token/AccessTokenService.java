package com.medlink.sms.service.token;

import com.google.gson.Gson;
import com.medlink.sms.entity.otp.SmsBranchName;
import com.medlink.sms.model.token.ResponseAccessToken;
import com.medlink.sms.service.otp.OtpService;
import com.medlink.sms.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class AccessTokenService {
    private static final Logger log = LogManager.getLogger(OtpService.class);
    private RestTemplate restTemplate = new RestTemplate();
    private ResponseEntity<String> result;

    @Value("${fpt_scope}")
    private String scope;

    @Value("${fpt_grant_type}")
    private String grantType;

    @Value("${fpt_connect_api_url}")
    private String connect_api_url;

    public String requestAccessToken(SmsBranchName smsBranchName) {
        JSONObject payload = new JSONObject();
        payload.put("client_id", smsBranchName.getClientId());
        payload.put("client_secret", smsBranchName.getClientSecret());
        payload.put("scope", scope);
        payload.put("grant_type", grantType);
        payload.put("session_id", "123213");

        String accessToken = "";
        try {
            log.info(JsonUtils.convertObjectToJson(payload));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<JSONObject> entity = new HttpEntity<JSONObject>(payload, headers);
            result = restTemplate.postForEntity(connect_api_url, entity, String.class);
            ResponseAccessToken responseAccessToken = new Gson().fromJson(result.getBody(), ResponseAccessToken.class);
            accessToken = responseAccessToken.getAccess_token();
            log.info(result.getStatusCode().value());
        } catch (HttpStatusCodeException e) {
            log.info(e.getMessage());
        }
        return accessToken;
    }
}
