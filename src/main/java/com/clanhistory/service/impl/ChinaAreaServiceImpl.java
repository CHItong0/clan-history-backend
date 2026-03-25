package com.clanhistory.service.impl;

import com.clanhistory.dto.CityDTO;
import com.clanhistory.dto.CoordinateDTO;
import com.clanhistory.dto.DistrictDTO;
import com.clanhistory.dto.ProvinceDTO;
import com.clanhistory.service.ChinaAreaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChinaAreaServiceImpl implements ChinaAreaService {

    private final ObjectMapper objectMapper;
    private JsonNode areaData;

    @PostConstruct
    public void init() {
        try {
            ClassPathResource resource = new ClassPathResource("data/china-areas.json");
            try (InputStream is = resource.getInputStream()) {
                areaData = objectMapper.readTree(is);
            }
        } catch (IOException e) {
            log.error("Failed to load china-areas.json", e);
        }
    }

    @Override
    public List<ProvinceDTO> getAllProvinces() {
        List<ProvinceDTO> provinces = new ArrayList<>();
        if (areaData == null || !areaData.has("provinces")) {
            return provinces;
        }
        for (JsonNode provinceNode : areaData.get("provinces")) {
            ProvinceDTO dto = new ProvinceDTO();
            dto.setCode(provinceNode.get("code").asText());
            dto.setName(provinceNode.get("name").asText());
            dto.setLng(provinceNode.has("lng") ? provinceNode.get("lng").asDouble() : null);
            dto.setLat(provinceNode.has("lat") ? provinceNode.get("lat").asDouble() : null);
            provinces.add(dto);
        }
        return provinces;
    }

    @Override
    public List<CityDTO> getCitiesByProvince(String provinceCode) {
        List<CityDTO> cities = new ArrayList<>();
        if (areaData == null || !areaData.has("provinces")) {
            return cities;
        }
        for (JsonNode provinceNode : areaData.get("provinces")) {
            if (provinceNode.get("code").asText().equals(provinceCode)) {
                if (provinceNode.has("cities")) {
                    for (JsonNode cityNode : provinceNode.get("cities")) {
                        CityDTO dto = new CityDTO();
                        dto.setCode(cityNode.get("code").asText());
                        dto.setName(cityNode.get("name").asText());
                        dto.setLng(cityNode.has("lng") ? cityNode.get("lng").asDouble() : null);
                        dto.setLat(cityNode.has("lat") ? cityNode.get("lat").asDouble() : null);
                        cities.add(dto);
                    }
                }
                break;
            }
        }
        return cities;
    }

    @Override
    public List<DistrictDTO> getDistrictsByCity(String cityCode) {
        List<DistrictDTO> districts = new ArrayList<>();
        if (areaData == null || !areaData.has("provinces")) {
            return districts;
        }
        for (JsonNode provinceNode : areaData.get("provinces")) {
            if (provinceNode.has("cities")) {
                for (JsonNode cityNode : provinceNode.get("cities")) {
                    if (cityNode.get("code").asText().equals(cityCode)) {
                        if (cityNode.has("districts")) {
                            for (JsonNode districtNode : cityNode.get("districts")) {
                                DistrictDTO dto = new DistrictDTO();
                                dto.setCode(districtNode.get("code").asText());
                                dto.setName(districtNode.get("name").asText());
                                dto.setLng(districtNode.has("lng") ? districtNode.get("lng").asDouble() : null);
                                dto.setLat(districtNode.has("lat") ? districtNode.get("lat").asDouble() : null);
                                districts.add(dto);
                            }
                        }
                        return districts;
                    }
                }
            }
        }
        return districts;
    }

    @Override
    public CoordinateDTO resolveCoordinate(String province, String city, String district) {
        if (areaData == null || !areaData.has("provinces")) {
            return null;
        }

        // Find province
        for (JsonNode provinceNode : areaData.get("provinces")) {
            if (provinceNode.get("name").asText().equals(province) ||
                provinceNode.get("code").asText().equals(province)) {

                // If no city specified, return province coordinates
                if (city == null || city.isEmpty()) {
                    CoordinateDTO coords = new CoordinateDTO();
                    coords.setLng(provinceNode.has("lng") ? provinceNode.get("lng").asDouble() : null);
                    coords.setLat(provinceNode.has("lat") ? provinceNode.get("lat").asDouble() : null);
                    return coords;
                }

                if (!provinceNode.has("cities")) continue;

                // Find city
                for (JsonNode cityNode : provinceNode.get("cities")) {
                    if (cityNode.get("name").asText().equals(city) ||
                        cityNode.get("code").asText().equals(city)) {

                        // If no district specified, return city coordinates
                        if (district == null || district.isEmpty()) {
                            CoordinateDTO coords = new CoordinateDTO();
                            coords.setLng(cityNode.has("lng") ? cityNode.get("lng").asDouble() : null);
                            coords.setLat(cityNode.has("lat") ? cityNode.get("lat").asDouble() : null);
                            return coords;
                        }

                        if (!cityNode.has("districts")) continue;

                        // Find district
                        for (JsonNode districtNode : cityNode.get("districts")) {
                            if (districtNode.get("name").asText().equals(district) ||
                                districtNode.get("code").asText().equals(district)) {
                                CoordinateDTO coords = new CoordinateDTO();
                                coords.setLng(districtNode.has("lng") ? districtNode.get("lng").asDouble() : null);
                                coords.setLat(districtNode.has("lat") ? districtNode.get("lat").asDouble() : null);
                                return coords;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
}
