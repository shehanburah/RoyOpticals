package com.theheavenscode.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DailyLog.
 */
@Document(collection = "daily_log")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "dailylog_a")
public class DailyLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("created_date")
    private LocalDate createdDate;

    @Field("rx")
    private Integer rx;

    @Field("invoice_id")
    private String invoiceId;

    @Field("soldering_job_invoice_id")
    private String solderingJobInvoiceId;

    @Field("customer")
    private String customer;

    @Field("customer_id")
    private String customerId;

    @Field("description")
    private String description;

    @Field("amount")
    private Double amount;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public DailyLog createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getRx() {
        return rx;
    }

    public DailyLog rx(Integer rx) {
        this.rx = rx;
        return this;
    }

    public void setRx(Integer rx) {
        this.rx = rx;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public DailyLog invoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSolderingJobInvoiceId() {
        return solderingJobInvoiceId;
    }

    public DailyLog solderingJobInvoiceId(String solderingJobInvoiceId) {
        this.solderingJobInvoiceId = solderingJobInvoiceId;
        return this;
    }

    public void setSolderingJobInvoiceId(String solderingJobInvoiceId) {
        this.solderingJobInvoiceId = solderingJobInvoiceId;
    }

    public String getCustomer() {
        return customer;
    }

    public DailyLog customer(String customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getCustomerId() {
        return customerId;
    }

    public DailyLog customerId(String customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDescription() {
        return description;
    }

    public DailyLog description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public DailyLog amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DailyLog)) {
            return false;
        }
        return id != null && id.equals(((DailyLog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DailyLog{" +
            "id=" + getId() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", rx=" + getRx() +
            ", invoiceId='" + getInvoiceId() + "'" +
            ", solderingJobInvoiceId='" + getSolderingJobInvoiceId() + "'" +
            ", customer='" + getCustomer() + "'" +
            ", customerId='" + getCustomerId() + "'" +
            ", description='" + getDescription() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
