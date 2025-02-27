package com.kcjs.cloud.gateway.controller;

import com.kcjs.cloud.gateway.feign.AddressServiceFeign;
import com.kcjs.cloud.pojo.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressController {

    private final AddressServiceFeign feign;


    public AddressController(AddressServiceFeign feign) {
        this.feign = feign;
    }


    @GetMapping("/province")
    List<BdProvince> provinceList() {
        return feign.provinceList();
    }

    @GetMapping("/city/{pId}")
    List<BdCity> cityList(@PathVariable(name = "pId") String pId) {
        return feign.cityList(pId);
    }

    @GetMapping("/region/{cId}")
    List<BdRegion> regionList(@PathVariable(name = "cId") String cId) {
        return feign.regionList(cId);
    }

    @GetMapping("/street/{rId}")
    List<CtPubStreet> streetList(@PathVariable(name = "rId") String rId) {
        return feign.streetList(rId);
    }

    @GetMapping("/streetRange/{sId}")
    List<CtPubStreetRange> streetRangeList(@PathVariable(name = "sId") String sId) {
        return feign.streetRangeList(sId);
    }
}
