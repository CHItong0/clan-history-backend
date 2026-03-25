package com.clanhistory.service;

import com.clanhistory.dto.CityDTO;
import com.clanhistory.dto.CoordinateDTO;
import com.clanhistory.dto.DistrictDTO;
import com.clanhistory.dto.ProvinceDTO;

import java.util.List;

public interface ChinaAreaService {
    List<ProvinceDTO> getAllProvinces();
    List<CityDTO> getCitiesByProvince(String provinceCode);
    List<DistrictDTO> getDistrictsByCity(String cityCode);
    CoordinateDTO resolveCoordinate(String province, String city, String district);
}
