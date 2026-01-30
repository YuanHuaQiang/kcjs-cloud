package com.kcjs.cloud.oracle.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @TableName CT_PUB_STREET
 */
@Data
@TableName("CT_PUB_STREET")
public class CtPubStreet implements Serializable {
    /**
     * 
     */
    @TableId
    private String fid;

    /**
     * 
     */
    private String fnumber;

    /**
     * 
     */
    private String fsimplename;

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
    private String cfregionid;

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
    private Integer cfdeletedstatus;

    private static final long serialVersionUID = 1L;
}