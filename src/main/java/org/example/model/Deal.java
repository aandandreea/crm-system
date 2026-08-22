package org.example.model;

import java.math.BigDecimal;

public class Deal {
    private long dealId;
    private String title;
    private BigDecimal amount;
    private DealStage stage;
    private long customerId;

    public Deal(long dealId, String title, BigDecimal amount, String stage, long customerId) {
    }

    public Deal(String title, BigDecimal amount, DealStage stage, long customerId) {
        this.title = title;
        this.amount = amount;
        this.stage = stage;
        this.customerId = customerId;
    }

    public Deal(long dealId, String title, BigDecimal amount, DealStage stage, long customerId) {
        this.dealId = dealId;
        this.title = title;
        this.amount = amount;
        this.stage = stage;
        this.customerId = customerId;
    }

    public long getDealId() {
        return dealId;
    }

    public void setDealId(long dealId) {
        this.dealId = dealId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public DealStage getStage() {
        return stage;
    }

    public void setStage(DealStage stage) {
        this.stage = stage;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "dealId=" + dealId +
                ", title='" + title + '\'' +
                ", amount=" + amount +
                ", stage=" + stage +
                ", customerId=" + customerId +
                '}';
    }
}
