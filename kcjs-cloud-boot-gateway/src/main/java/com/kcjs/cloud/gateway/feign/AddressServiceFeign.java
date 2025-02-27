package com.kcjs.cloud.gateway.feign;


import com.kcjs.cloud.pojo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "kcjs-cloud-dubbo-consumer",contextId = "AddressServiceFeign")
public interface AddressServiceFeign {

    @GetMapping("/province")
    List<BdProvince> provinceList() ;

    @GetMapping("/city/{pId}")
    List<BdCity> cityList(@PathVariable(name = "pId") String pId) ;

    @GetMapping("/region/{cId}")
    List<BdRegion> regionList(@PathVariable(name = "cId") String cId) ;

    @GetMapping("/street/{rId}")
    List<CtPubStreet> streetList(@PathVariable(name = "rId") String rId) ;

    @GetMapping("/streetRange/{sId}")
    List<CtPubStreetRange> streetRangeList(@PathVariable(name = "sId") String sId) ;
}
