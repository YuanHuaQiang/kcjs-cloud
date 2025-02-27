package com.kcjs.cloud.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 区域范围表
 * @TableName CT_PUB_STREET_RANGE
 */
@ApiModel(description = "区域范围表")
@TableName(value ="CT_PUB_STREET_RANGE")
@Data
public class CtPubStreetRange implements Serializable {
    /**
     *
     */
    @ApiModelProperty("")
    @TableId
    private String id;

    /**
     * 区域范围中文名称

     */
    @ApiModelProperty("区域范围中文名称")
    private String name;

    /**
     * 区域范围编号

     */
    @ApiModelProperty("区域范围编号")
    private String fNumber;

    /**
     * 街道ID

     */
    @ApiModelProperty("街道ID")
    private String parentId;

    @ApiModelProperty(hidden = true)
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
