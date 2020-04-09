package com.theheavenscode.domain;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PettyCashStock.
 */
@Document(collection = "petty_cash_stock")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pettycashstock_a")
public class PettyCashStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private String id;

    @Field("item_name")
    private String itemName;

    @Field("brand_name")
    private String brandName;

    @Field("model_number")
    private String modelNumber;

    @Field("type")
    private String type;

    @Field("size")
    private Integer size;

    @Field("remarks")
    private String remarks;

    @Field("quantity")
    private Integer quantity;

    @Field("price")
    private Integer price;

    @Field("discounted_price")
    private Integer discountedPrice;

    @Field("stock_in_date")
    private LocalDate stockInDate;

    @Field("est_next_stock_in_date")
    private LocalDate estNextStockInDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public PettyCashStock itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public PettyCashStock brandName(String brandName) {
        this.brandName = brandName;
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public PettyCashStock modelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
        return this;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getType() {
        return type;
    }

    public PettyCashStock type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public PettyCashStock size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getRemarks() {
        return remarks;
    }

    public PettyCashStock remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public PettyCashStock quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public PettyCashStock price(Integer price) {
        this.price = price;
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getDiscountedPrice() {
        return discountedPrice;
    }

    public PettyCashStock discountedPrice(Integer discountedPrice) {
        this.discountedPrice = discountedPrice;
        return this;
    }

    public void setDiscountedPrice(Integer discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public LocalDate getStockInDate() {
        return stockInDate;
    }

    public PettyCashStock stockInDate(LocalDate stockInDate) {
        this.stockInDate = stockInDate;
        return this;
    }

    public void setStockInDate(LocalDate stockInDate) {
        this.stockInDate = stockInDate;
    }

    public LocalDate getEstNextStockInDate() {
        return estNextStockInDate;
    }

    public PettyCashStock estNextStockInDate(LocalDate estNextStockInDate) {
        this.estNextStockInDate = estNextStockInDate;
        return this;
    }

    public void setEstNextStockInDate(LocalDate estNextStockInDate) {
        this.estNextStockInDate = estNextStockInDate;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PettyCashStock)) {
            return false;
        }
        return id != null && id.equals(((PettyCashStock) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PettyCashStock{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", modelNumber='" + getModelNumber() + "'" +
            ", type='" + getType() + "'" +
            ", size=" + getSize() +
            ", remarks='" + getRemarks() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", discountedPrice=" + getDiscountedPrice() +
            ", stockInDate='" + getStockInDate() + "'" +
            ", estNextStockInDate='" + getEstNextStockInDate() + "'" +
            "}";
    }
}
