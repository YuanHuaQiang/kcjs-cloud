package com.kcjs.cloud.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kcjs.cloud.pojo.BdCity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description 市
 * @Date 2021/5/7 11:51
 * @Author yuanhuaqiang.work@gmail.com
 **/
@Component
public interface BdCityMapper extends BaseMapper<BdCity> {

    @Select(" select * from t_bd_city where fname_l2 = #{cityName}")
    BdCity getCityByName(String cityName);

    List<BdCity> getCityListByName(String cityName);


    int insertNewTable(@Param("newTable") String newTable, @Param("entity") BdCity entity);


    int batchInsert(@Param("newTable") String newTable ,@Param("list") List<BdCity> list);
}




