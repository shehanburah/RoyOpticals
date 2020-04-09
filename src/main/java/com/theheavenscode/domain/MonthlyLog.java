package com.theheavenscode.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A MonthlyLog.
 */
@Document(collection = "monthly_log")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "monthlylog_a")
public class MonthlyLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("identification")
    private Integer identification;

    @Field("report_obtained_by")
    private String reportObtainedBy;

    @Field("created_date")
    private LocalDate createdDate;

    @Field("amount")
    private Integer amount;

    @Field("remark")
    private String remark;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIdentification() {
        return identification;
    }

    public MonthlyLog identification(Integer identification) {
        this.identification = identification;
        return this;
    }

    public void setIdentification(Integer identification) {
        this.identification = identification;
    }

    public String getReportObtainedBy() {
        return reportObtainedBy;
    }

    public MonthlyLog reportObtainedBy(String reportObtainedBy) {
        this.reportObtainedBy = reportObtainedBy;
        return this;
    }

    public void setReportObtainedBy(String reportObtainedBy) {
        this.reportObtainedBy = reportObtainedBy;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public MonthlyLog createdDate(LocalDate createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getAmount() {
        return amount;
    }

    public MonthlyLog amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public MonthlyLog remark(String remark) {
        this.remark = remark;
        return this;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonthlyLog)) {
            return false;
        }
        return id != null && id.equals(((MonthlyLog) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MonthlyLog{" +
            "id=" + getId() +
            ", identification=" + getIdentification() +
            ", reportObtainedBy='" + getReportObtainedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", amount=" + getAmount() +
            ", remark='" + getRemark() + "'" +
            "}";
    }
}
