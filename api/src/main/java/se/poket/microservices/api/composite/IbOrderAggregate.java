package se.poket.microservices.api.composite;

import java.util.Date;

public class IbOrderAggregate {
    private int orderId;
    private String symbol;
    private String exchange;
    private String currency;
    private String type;
    private Double lastFillPrice;
    private int numContracts;
    private String status;
    private Date createdDate;
    private Date sentDate;
    private Date lastUpdateDate;

    private ServiceAddresses serviceAddresses;

    public IbOrderAggregate() {
        this.orderId = 0;
        this.symbol = null;
        this.exchange = null;
        this.currency = null;
        this.type = null;
        this.lastFillPrice = 0.00;
        this.numContracts = 0;
        this.status = null;
        this.createdDate = null;
        this.sentDate = null;
        this.lastUpdateDate = null;
        this.serviceAddresses = null;
    }

    public IbOrderAggregate(int orderId, String symbol, String exchange, String currency, String type, Double lastFillPrice, int numContracts, String status, Date createdDate, Date sentDate, Date lastUpdateDate, ServiceAddresses serviceAddresses) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.exchange = exchange;
        this.currency = currency;
        this.type = type;
        this.lastFillPrice = lastFillPrice;
        this.numContracts = numContracts;
        this.status = status;
        this.createdDate = createdDate;
        this.sentDate = sentDate;
        this.lastUpdateDate = lastUpdateDate;
        this.serviceAddresses = serviceAddresses;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getLastFillPrice() {
        return lastFillPrice;
    }

    public void setLastFillPrice(Double lastFillPrice) {
        this.lastFillPrice = lastFillPrice;
    }

    public int getNumContracts() {
        return numContracts;
    }

    public void setNumContracts(int numContracts) {
        this.numContracts = numContracts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public ServiceAddresses getServiceAddresses() {
        return serviceAddresses;
    }

    public void setServiceAddresses(ServiceAddresses serviceAddresses) {
        this.serviceAddresses = serviceAddresses;
    }
}
