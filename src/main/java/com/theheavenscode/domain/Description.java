package com.theheavenscode.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Description.
 */
@Document(collection = "description")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "description_a")
public class Description implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("customer_id")
    private String customerId;

    @Field("invoice_id")
    private String invoiceId;

    @Field("sph_left")
    private String sphLeft;

    @Field("cyl_left")
    private String cylLeft;

    @Field("axis_left")
    private String axisLeft;

    @Field("reading_left")
    private String readingLeft;

    @Field("sph_right")
    private String sphRight;

    @Field("cyl_right")
    private String cylRight;

    @Field("axis_right")
    private String axisRight;

    @Field("reading_right")
    private String readingRight;

    @Field("order_date")
    private Instant orderDate;

    @Field("delevery_date")
    private Instant deleveryDate;

    @Field("order_no")
    private String orderNo;

    @Field("frame")
    private String frame;

    @Field("model_no")
    private String modelNo;

    @Field("size")
    private String size;

    @Field("colour")
    private String colour;

    @Field("l_type")
    private String lType;

    @Field("l_size")
    private String lSize;

    @Field("pd")
    private String pd;

    @Field("inset")
    private String inset;

    @Field("name")
    private String name;

    @Field("remarks")
    private String remarks;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Description customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public Description invoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSphLeft() {
        return sphLeft;
    }

    public Description sphLeft(String sphLeft) {
        this.sphLeft = sphLeft;
        return this;
    }

    public void setSphLeft(String sphLeft) {
        this.sphLeft = sphLeft;
    }

    public String getCylLeft() {
        return cylLeft;
    }

    public Description cylLeft(String cylLeft) {
        this.cylLeft = cylLeft;
        return this;
    }

    public void setCylLeft(String cylLeft) {
        this.cylLeft = cylLeft;
    }

    public String getAxisLeft() {
        return axisLeft;
    }

    public Description axisLeft(String axisLeft) {
        this.axisLeft = axisLeft;
        return this;
    }

    public void setAxisLeft(String axisLeft) {
        this.axisLeft = axisLeft;
    }

    public String getReadingLeft() {
        return readingLeft;
    }

    public Description readingLeft(String readingLeft) {
        this.readingLeft = readingLeft;
        return this;
    }

    public void setReadingLeft(String readingLeft) {
        this.readingLeft = readingLeft;
    }

    public String getSphRight() {
        return sphRight;
    }

    public Description sphRight(String sphRight) {
        this.sphRight = sphRight;
        return this;
    }

    public void setSphRight(String sphRight) {
        this.sphRight = sphRight;
    }

    public String getCylRight() {
        return cylRight;
    }

    public Description cylRight(String cylRight) {
        this.cylRight = cylRight;
        return this;
    }

    public void setCylRight(String cylRight) {
        this.cylRight = cylRight;
    }

    public String getAxisRight() {
        return axisRight;
    }

    public Description axisRight(String axisRight) {
        this.axisRight = axisRight;
        return this;
    }

    public void setAxisRight(String axisRight) {
        this.axisRight = axisRight;
    }

    public String getReadingRight() {
        return readingRight;
    }

    public Description readingRight(String readingRight) {
        this.readingRight = readingRight;
        return this;
    }

    public void setReadingRight(String readingRight) {
        this.readingRight = readingRight;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public Description orderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Instant getDeleveryDate() {
        return deleveryDate;
    }

    public Description deleveryDate(Instant deleveryDate) {
        this.deleveryDate = deleveryDate;
        return this;
    }

    public void setDeleveryDate(Instant deleveryDate) {
        this.deleveryDate = deleveryDate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public Description orderNo(String orderNo) {
        this.orderNo = orderNo;
        return this;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getFrame() {
        return frame;
    }

    public Description frame(String frame) {
        this.frame = frame;
        return this;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getModelNo() {
        return modelNo;
    }

    public Description modelNo(String modelNo) {
        this.modelNo = modelNo;
        return this;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getSize() {
        return size;
    }

    public Description size(String size) {
        this.size = size;
        return this;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColour() {
        return colour;
    }

    public Description colour(String colour) {
        this.colour = colour;
        return this;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getlType() {
        return lType;
    }

    public Description lType(String lType) {
        this.lType = lType;
        return this;
    }

    public void setlType(String lType) {
        this.lType = lType;
    }

    public String getlSize() {
        return lSize;
    }

    public Description lSize(String lSize) {
        this.lSize = lSize;
        return this;
    }

    public void setlSize(String lSize) {
        this.lSize = lSize;
    }

    public String getPd() {
        return pd;
    }

    public Description pd(String pd) {
        this.pd = pd;
        return this;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getInset() {
        return inset;
    }

    public Description inset(String inset) {
        this.inset = inset;
        return this;
    }

    public void setInset(String inset) {
        this.inset = inset;
    }

    public String getName() {
        return name;
    }

    public Description name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public Description remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Description)) {
            return false;
        }
        return id != null && id.equals(((Description) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Description{" +
            "id=" + getId() +
            ", customerId='" + getCustomerId() + "'" +
            ", invoiceId='" + getInvoiceId() + "'" +
            ", sphLeft='" + getSphLeft() + "'" +
            ", cylLeft='" + getCylLeft() + "'" +
            ", axisLeft='" + getAxisLeft() + "'" +
            ", readingLeft='" + getReadingLeft() + "'" +
            ", sphRight='" + getSphRight() + "'" +
            ", cylRight='" + getCylRight() + "'" +
            ", axisRight='" + getAxisRight() + "'" +
            ", readingRight='" + getReadingRight() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", deleveryDate='" + getDeleveryDate() + "'" +
            ", orderNo='" + getOrderNo() + "'" +
            ", frame='" + getFrame() + "'" +
            ", modelNo='" + getModelNo() + "'" +
            ", size='" + getSize() + "'" +
            ", colour='" + getColour() + "'" +
            ", lType='" + getlType() + "'" +
            ", lSize='" + getlSize() + "'" +
            ", pd='" + getPd() + "'" +
            ", inset='" + getInset() + "'" +
            ", name='" + getName() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
