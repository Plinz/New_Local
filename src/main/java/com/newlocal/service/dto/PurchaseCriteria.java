package com.newlocal.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the Purchase entity. This class is used in PurchaseResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /purchases?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PurchaseCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter saleDate;

    private IntegerFilter quantity;

    private BooleanFilter withdraw;

    private LongFilter stockId;

    private LongFilter clientId;

    public PurchaseCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(InstantFilter saleDate) {
        this.saleDate = saleDate;
    }

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public BooleanFilter getWithdraw() {
        return withdraw;
    }

    public void setWithdraw(BooleanFilter withdraw) {
        this.withdraw = withdraw;
    }

    public LongFilter getStockId() {
        return stockId;
    }

    public void setStockId(LongFilter stockId) {
        this.stockId = stockId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PurchaseCriteria that = (PurchaseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(saleDate, that.saleDate) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(withdraw, that.withdraw) &&
            Objects.equals(stockId, that.stockId) &&
            Objects.equals(clientId, that.clientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        saleDate,
        quantity,
        withdraw,
        stockId,
        clientId
        );
    }

    @Override
    public String toString() {
        return "PurchaseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (saleDate != null ? "saleDate=" + saleDate + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (withdraw != null ? "withdraw=" + withdraw + ", " : "") +
                (stockId != null ? "stockId=" + stockId + ", " : "") +
                (clientId != null ? "clientId=" + clientId + ", " : "") +
            "}";
    }

}
