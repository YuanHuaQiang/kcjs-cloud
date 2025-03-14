package com.kcjs.cloud.oracle.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName T_BD_CITY
 */
@TableName(value ="T_BD_CITY")
@Data
public class BdCity implements Serializable {
    /**
     *
     */
    @TableId
    private String fid;

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
    private String fprovinceid;

    /**
     *
     */
    private Integer fisdircity;

    /**
     *
     */
    private String fcitynumber;

    /**
     *
     */
    private Integer fdeletedstatus;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
