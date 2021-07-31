package org.tonylin.stock.dao.po;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class MopsInvoiceIncomingPK implements Serializable {

    /** identifier field */
    private String stock;

    /** identifier field */
    private String year;

    /** identifier field */
    private String month;

    /** full constructor */
    public MopsInvoiceIncomingPK(String stock, String year, String month) {
        this.stock = stock;
        this.year = year;
        this.month = month;
    }

    /** default constructor */
    public MopsInvoiceIncomingPK() {
    }

    /** 
     *                @hibernate.property
     *                 column="stock"
     *             
     */
    public String getStock() {
        return this.stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    /** 
     *                @hibernate.property
     *                 column="year"
     *             
     */
    public String getYear() {
        return this.year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    /** 
     *                @hibernate.property
     *                 column="month"
     *             
     */
    public String getMonth() {
        return this.month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("stock", getStock())
            .append("year", getYear())
            .append("month", getMonth())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof MopsInvoiceIncomingPK) ) return false;
        MopsInvoiceIncomingPK castOther = (MopsInvoiceIncomingPK) other;
        return new EqualsBuilder()
            .append(this.getStock(), castOther.getStock())
            .append(this.getYear(), castOther.getYear())
            .append(this.getMonth(), castOther.getMonth())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getStock())
            .append(getYear())
            .append(getMonth())
            .toHashCode();
    }

}
