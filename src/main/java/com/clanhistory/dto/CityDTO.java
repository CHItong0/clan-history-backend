package com.clanhistory.dto;

import lombok.Data;

@Data
public class CityDTO {
    private String code;
    private String name;
    private Double lng;
    private Double lat;
}
