package com.kcjs.cloud.provider.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kcjs.cloud.api.address.AddressService;
import com.kcjs.cloud.mapper.*;
import com.kcjs.cloud.pojo.*;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class AddressServiceImpl implements AddressService {


    @Autowired
    private BdProvinceMapper bdProvinceMapper;


    @Autowired
    private BdCityMapper bdCityMapper;


    @Autowired
    private BdRegionMapper bdRegionMapper;


    @Autowired
    private CtPubStreetMapper ctPubStreetMapper;


    @Autowired
    private CtPubStreetRangeMapper ctPubStreetRangeMapper;


    @Override
    public List<BdProvince> provinceList() {
        return bdProvinceMapper.selectList(null);
    }

    @Override
    public List<BdCity> cityList(String pId) {
        return bdCityMapper.selectList(new QueryWrapper<BdCity>().eq("fprovinceid",pId).orderByAsc("fnumber"));
    }

    @Override
    public List<BdRegion> regionList(String cId) {
        return bdRegionMapper.selectList(new QueryWrapper<BdRegion>().eq("fcityid",cId).orderByAsc("fnumber"));
    }

    @Override
    public List<CtPubStreet> streetList(String rId) {
        return ctPubStreetMapper.selectList(new QueryWrapper<CtPubStreet>().eq("cfregionid",rId).orderByAsc("fnumber"));
    }

    @Override
    public List<CtPubStreetRange> streetRangeList(String sId) {
        return ctPubStreetRangeMapper.selectList(new QueryWrapper<CtPubStreetRange>().eq("parent_id",sId).orderByAsc("f_number"));
    }
}
