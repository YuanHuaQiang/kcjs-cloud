package com.kcjs.cloud.api.address;

import com.kcjs.cloud.pojo.*;

import java.util.List;

public interface AddressService {
    List<BdProvince> provinceList();
    List<BdCity> cityList(String pId);
    List<BdRegion> regionList(String cId);
    List<CtPubStreet> streetList(String rId);
    List<CtPubStreetRange> streetRangeList(String sId);
}