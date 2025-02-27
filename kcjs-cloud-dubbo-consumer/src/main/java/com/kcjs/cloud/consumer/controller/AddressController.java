package com.kcjs.cloud.consumer.controller;

import com.kcjs.cloud.api.address.AddressService;
import com.kcjs.cloud.pojo.*;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AddressController {

    @DubboReference
    private AddressService addressService;


    @GetMapping("/province")
    List<BdProvince> provinceList() {
        return addressService.provinceList();
    }

    @GetMapping("/city/{pId}")
    List<BdCity> cityList(@PathVariable(name = "pId") String pId) {
        return addressService.cityList(pId);
    }

    @GetMapping("/region/{cId}")
    List<BdRegion> regionList(@PathVariable(name = "cId") String cId) {
        return addressService.regionList(cId);
    }

    @GetMapping("/street/{rId}")
    List<CtPubStreet> streetList(@PathVariable(name = "rId") String rId) {
        return addressService.streetList(rId);
    }

    @GetMapping("/streetRange/{sId}")
    List<CtPubStreetRange> streetRangeList(@PathVariable(name = "sId") String sId) {
        return addressService.streetRangeList(sId);
    }
}
