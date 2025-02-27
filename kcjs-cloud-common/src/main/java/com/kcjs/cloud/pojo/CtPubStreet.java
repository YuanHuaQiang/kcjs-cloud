package com.kcjs.cloud.pojo;

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

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        CtPubStreet other = (CtPubStreet) that;
        return (this.getFid() == null ? other.getFid() == null : this.getFid().equals(other.getFid()))
            && (this.getFnumber() == null ? other.getFnumber() == null : this.getFnumber().equals(other.getFnumber()))
            && (this.getFsimplename() == null ? other.getFsimplename() == null : this.getFsimplename().equals(other.getFsimplename()))
            && (this.getFcreatorid() == null ? other.getFcreatorid() == null : this.getFcreatorid().equals(other.getFcreatorid()))
            && (this.getFcreatetime() == null ? other.getFcreatetime() == null : this.getFcreatetime().equals(other.getFcreatetime()))
            && (this.getFlastupdateuserid() == null ? other.getFlastupdateuserid() == null : this.getFlastupdateuserid().equals(other.getFlastupdateuserid()))
            && (this.getFlastupdatetime() == null ? other.getFlastupdatetime() == null : this.getFlastupdatetime().equals(other.getFlastupdatetime()))
            && (this.getFcontrolunitid() == null ? other.getFcontrolunitid() == null : this.getFcontrolunitid().equals(other.getFcontrolunitid()))
            && (this.getCfregionid() == null ? other.getCfregionid() == null : this.getCfregionid().equals(other.getCfregionid()))
            && (this.getFnameL1() == null ? other.getFnameL1() == null : this.getFnameL1().equals(other.getFnameL1()))
            && (this.getFnameL2() == null ? other.getFnameL2() == null : this.getFnameL2().equals(other.getFnameL2()))
            && (this.getFnameL3() == null ? other.getFnameL3() == null : this.getFnameL3().equals(other.getFnameL3()))
            && (this.getFdescriptionL1() == null ? other.getFdescriptionL1() == null : this.getFdescriptionL1().equals(other.getFdescriptionL1()))
            && (this.getFdescriptionL2() == null ? other.getFdescriptionL2() == null : this.getFdescriptionL2().equals(other.getFdescriptionL2()))
            && (this.getFdescriptionL3() == null ? other.getFdescriptionL3() == null : this.getFdescriptionL3().equals(other.getFdescriptionL3()))
            && (this.getCfdeletedstatus() == null ? other.getCfdeletedstatus() == null : this.getCfdeletedstatus().equals(other.getCfdeletedstatus()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getFid() == null) ? 0 : getFid().hashCode());
        result = prime * result + ((getFnumber() == null) ? 0 : getFnumber().hashCode());
        result = prime * result + ((getFsimplename() == null) ? 0 : getFsimplename().hashCode());
        result = prime * result + ((getFcreatorid() == null) ? 0 : getFcreatorid().hashCode());
        result = prime * result + ((getFcreatetime() == null) ? 0 : getFcreatetime().hashCode());
        result = prime * result + ((getFlastupdateuserid() == null) ? 0 : getFlastupdateuserid().hashCode());
        result = prime * result + ((getFlastupdatetime() == null) ? 0 : getFlastupdatetime().hashCode());
        result = prime * result + ((getFcontrolunitid() == null) ? 0 : getFcontrolunitid().hashCode());
        result = prime * result + ((getCfregionid() == null) ? 0 : getCfregionid().hashCode());
        result = prime * result + ((getFnameL1() == null) ? 0 : getFnameL1().hashCode());
        result = prime * result + ((getFnameL2() == null) ? 0 : getFnameL2().hashCode());
        result = prime * result + ((getFnameL3() == null) ? 0 : getFnameL3().hashCode());
        result = prime * result + ((getFdescriptionL1() == null) ? 0 : getFdescriptionL1().hashCode());
        result = prime * result + ((getFdescriptionL2() == null) ? 0 : getFdescriptionL2().hashCode());
        result = prime * result + ((getFdescriptionL3() == null) ? 0 : getFdescriptionL3().hashCode());
        result = prime * result + ((getCfdeletedstatus() == null) ? 0 : getCfdeletedstatus().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", fid=").append(fid);
        sb.append(", fnumber=").append(fnumber);
        sb.append(", fsimplename=").append(fsimplename);
        sb.append(", fcreatorid=").append(fcreatorid);
        sb.append(", fcreatetime=").append(fcreatetime);
        sb.append(", flastupdateuserid=").append(flastupdateuserid);
        sb.append(", flastupdatetime=").append(flastupdatetime);
        sb.append(", fcontrolunitid=").append(fcontrolunitid);
        sb.append(", cfregionid=").append(cfregionid);
        sb.append(", fnameL1=").append(fnameL1);
        sb.append(", fnameL2=").append(fnameL2);
        sb.append(", fnameL3=").append(fnameL3);
        sb.append(", fdescriptionL1=").append(fdescriptionL1);
        sb.append(", fdescriptionL2=").append(fdescriptionL2);
        sb.append(", fdescriptionL3=").append(fdescriptionL3);
        sb.append(", cfdeletedstatus=").append(cfdeletedstatus);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}