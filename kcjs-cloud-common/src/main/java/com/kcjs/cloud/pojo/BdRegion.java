package com.kcjs.cloud.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName T_BD_REGION
 */
@TableName(value ="T_BD_REGION")
@Data
public class BdRegion implements Serializable {
    /**
     * 
     */
    @TableId
    private String fid;

    /**
     * 
     */
    private String fnameL1;

    /**
     * 
     */
    private String fnameL2;

    /**
     * 
     */
    private String fnameL3;

    /**
     * 
     */
    private String fnumber;

    /**
     * 
     */
    private String fdescriptionL1;

    /**
     * 
     */
    private String fdescriptionL2;

    /**
     * 
     */
    private String fdescriptionL3;

    /**
     * 
     */
    private String fsimplename;

    /**
     * 
     */
    private String fcityid;

    /**
     * 
     */
    private String fcreatorid;

    /**
     * 
     */
    private Date fcreatetime;

    /**
     * 
     */
    private String flastupdateuserid;

    /**
     * 
     */
    private Date flastupdatetime;

    /**
     * 
     */
    private String fcontrolunitid;

    /**
     * 
     */
    private Integer fdeletedstatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}