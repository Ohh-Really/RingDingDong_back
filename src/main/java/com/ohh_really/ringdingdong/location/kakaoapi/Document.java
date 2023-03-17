package com.ohh_really.ringdingdong.location.kakaoapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Document {
    @JsonProperty("region_type")
    public String regionType;
    public String code;
    @JsonProperty("address_name")
    public String addressName;
    @JsonProperty("region_1depth_name")
    public String region1depthName;
    @JsonProperty("region_2depth_name")
    public String region2depthName;
    @JsonProperty("region_3depth_name")
    public String region3depthName;
    @JsonProperty("region_4depth_name")
    public String region4depthName;
    public double x;
    public double y;
}
