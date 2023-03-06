package com.ohh_really.ringdingdong.location.kakaoapi;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Root{
    public Meta meta;
    public ArrayList<Document> documents;
}

