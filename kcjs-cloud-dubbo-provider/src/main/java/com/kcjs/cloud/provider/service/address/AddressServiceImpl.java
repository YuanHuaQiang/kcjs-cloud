package com.kcjs.cloud.provider.service.address;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kcjs.cloud.api.address.AddressService;
import com.kcjs.cloud.oracle.mapper.*;
import com.kcjs.cloud.oracle.pojo.*;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

@DubboService
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {


    private final BdProvinceMapper bdProvinceMapper;
    private final BdCityMapper bdCityMapper;
    private final BdRegionMapper bdRegionMapper;
    private final CtPubStreetMapper ctPubStreetMapper;
    private final CtPubStreetRangeMapper ctPubStreetRangeMapper;


    @Override
    public List<BdProvince> provinceList() {
        return bdProvinceMapper.selectList(null);
    }

    @Override
    public List<BdCity> cityList(String pId) {
        return bdCityMapper.selectList(new QueryWrapper<BdCity>().eq("fprovinceid", pId).orderByAsc("fnumber"));
    }

    @Override
    public List<BdRegion> regionList(String cId) {
        return bdRegionMapper.selectList(new QueryWrapper<BdRegion>().eq("fcityid", cId).orderByAsc("fnumber"));
    }

    @Override
    public List<CtPubStreet> streetList(String rId) {
        return ctPubStreetMapper.selectList(new QueryWrapper<CtPubStreet>().eq("cfregionid", rId).orderByAsc("fnumber"));
    }

    @Override
    public List<CtPubStreetRange> streetRangeList(String sId) {
        return ctPubStreetRangeMapper.selectList(new QueryWrapper<CtPubStreetRange>().eq("parent_id", sId).orderByAsc("f_number"));
    }
}
