package com.example.deidentification.utils;

import org.springframework.beans.factory.annotation.Value;

// https://hitomis.tistory.com/141 패키지 제작해보장
public class SFTPUtil {

    @Value("${nas.host}")
    private String host;

    @Value("${nas.port}")
    private int port;

    @Value("${nas.userId}")
    private String id;

    @Value("${nas.userPw}")
    private String pw;

    





}
