package com.theheavenscode.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A SolderingJobInvoice.
 */
@Document(collection = "soldering_job_invoice")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "solderingjobinvoice_a")
public class SolderingJobInvoice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("customer_id")
    private String customerId;

    @Field("price")
    private Double price;

    @Field("order_date")
    private LocalDate orderDate;

    @Field("delivered")
    private Boolean delivered;

    @Field("delivery_date")
    private LocalDate deliveryDate;

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

    public SolderingJobInvoice customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getPrice() {
        return price;
    }

    public SolderingJobInvoice price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public SolderingJobInvoice orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public Boolean isDelivered() {
        return delivered;
    }

    public SolderingJobInvoice delivered(Boolean delivered) {
        this.delivered = delivered;
        return this;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public SolderingJobInvoice deliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SolderingJobInvoice)) {
            return false;
        }
        return id != null && id.equals(((SolderingJobInvoice) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SolderingJobInvoice{" +
            "id=" + getId() +
            ", customerId='" + getCustomerId() + "'" +
            ", price=" + getPrice() +
            ", orderDate='" + getOrderDate() + "'" +
            ", delivered='" + isDelivered() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            "}";
    }
}
