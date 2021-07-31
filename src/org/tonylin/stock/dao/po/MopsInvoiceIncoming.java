package org.tonylin.stock.dao.po;

import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="mops_invoice_incoming"
 *     
*/
public class MopsInvoiceIncoming implements Serializable {

    /** identifier field */
    private org.tonylin.stock.dao.po.MopsInvoiceIncomingPK comp_id;

    /** nullable persistent field */
    private String invoice;

    /** nullable persistent field */
    private String bi;

    /** nullable persistent field */
    private String cbi;

    /** full constructor */
    public MopsInvoiceIncoming(org.tonylin.stock.dao.po.MopsInvoiceIncomingPK comp_id, String invoice, String bi, String cbi) {
        this.comp_id = comp_id;
        this.invoice = invoice;
        this.bi = bi;
        this.cbi = cbi;
    }

    /** default constructor */
    public MopsInvoiceIncoming() {
    }

    /** minimal constructor */
    public MopsInvoiceIncoming(org.tonylin.stock.dao.po.MopsInvoiceIncomingPK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.id
     *             generator-class="assigned"
     *         
     */
    public org.tonylin.stock.dao.po.MopsInvoiceIncomingPK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(org.tonylin.stock.dao.po.MopsInvoiceIncomingPK comp_id) {
        this.comp_id = comp_id;
    }

    /** 
     *            @hibernate.property
     *             column="invoice"
     *             length="128"
     *         
     */
    public String getInvoice() {
        return this.invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    /** 
     *            @hibernate.property
     *             column="bi"
     *             length="128"
     *         
     */
    public String getBi() {
        return this.bi;
    }

    public void setBi(String bi) {
        this.bi = bi;
    }

    /** 
     *            @hibernate.property
     *             column="cbi"
     *             length="128"
     *         
     */
    public String getCbi() {
        return this.cbi;
    }

    public void setCbi(String cbi) {
        this.cbi = cbi;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("comp_id", getComp_id())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof MopsInvoiceIncoming) ) return false;
        MopsInvoiceIncoming castOther = (MopsInvoiceIncoming) other;
        return new EqualsBuilder()
            .append(this.getComp_id(), castOther.getComp_id())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getComp_id())
            .toHashCode();
    }

}
