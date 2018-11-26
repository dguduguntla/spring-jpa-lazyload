package com.gdp.lazyload.domain;

import lombok.Data;

@Data
public class LocationDTO {
    private String streetAddress;
    private String postalCode;
    private String city;
    private String stateProvince;
    private String countryName;
    private String countryCode;
    private String countryRegionName;
}
