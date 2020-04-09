package com.theheavenscode.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Invoice.
 */
@Document(collection = "invoice")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "invoice_a")
public class Invoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("customer_id")
    private String customerId;

    @Field("rx")
    private Integer rx;

    @Field("delevery_date")
    private Instant deleveryDate;

    @Field("order_date")
    private Instant orderDate;

    @Field("advance")
    private Double advance;

    @Field("balance")
    private Double balance;

    @Field("delivered")
    private Boolean delivered;

    @Field("paid")
    private Boolean paid;

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

    public Invoice customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getRx() {
        return rx;
    }

    public Invoice rx(Integer rx) {
        this.rx = rx;
        return this;
    }

    public void setRx(Integer rx) {
        this.rx = rx;
    }

    public Instant getDeleveryDate() {
        return deleveryDate;
    }

    public Invoice deleveryDate(Instant deleveryDate) {
        this.deleveryDate = deleveryDate;
        return this;
    }

    public void setDeleveryDate(Instant deleveryDate) {
        this.deleveryDate = deleveryDate;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public Invoice orderDate(Instant orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Double getAdvance() {
        return advance;
    }

    public Invoice advance(Double advance) {
        this.advance = advance;
        return this;
    }

    public void setAdvance(Double advance) {
        this.advance = advance;
    }

    public Double getBalance() {
        return balance;
    }

    public Invoice balance(Double balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean isDelivered() {
        return delivered;
    }

    public Invoice delivered(Boolean delivered) {
        this.delivered = delivered;
        return this;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public Boolean isPaid() {
        return paid;
    }

    public Invoice paid(Boolean paid) {
        this.paid = paid;
        return this;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Invoice)) {
            return false;
        }
        return id != null && id.equals(((Invoice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Invoice{" +
            "id=" + getId() +
            ", customerId='" + getCustomerId() + "'" +
            ", rx=" + getRx() +
            ", deleveryDate='" + getDeleveryDate() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", advance=" + getAdvance() +
            ", balance=" + getBalance() +
            ", delivered='" + isDelivered() + "'" +
            ", paid='" + isPaid() + "'" +
            "}";
    }
}
