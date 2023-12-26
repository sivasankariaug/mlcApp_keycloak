package com.mlc.entity;

import lombok.Data;

import java.util.List;

@Data
public class Client {
    private String clientId;
    private boolean enabled;
    private List<String> redirectUris;
    private  List<String> webOrigins;
    private boolean publicClient;
}

