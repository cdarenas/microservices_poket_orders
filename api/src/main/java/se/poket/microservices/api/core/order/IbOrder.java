package se.poket.microservices.api.core.order;

import java.util.Date;

public class IbOrder {
    private int orderId;
    private int ibOrderId;
    private int clientId;
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
    private String serviceAddress;

    public IbOrder() {
        this.orderId = 0;
        this.ibOrderId = 0;
        this.symbol = null;
        this.exchange = "SMART";
        this.currency = "USD";
        this.type = "STK";
        this.lastFillPrice = null;
        this.numContracts = 0;
        this.status = "Pending";
        this.createdDate = null;
        this.sentDate = null;
        this.lastUpdateDate = null;
        this.serviceAddress = null;
    }

    public IbOrder(int orderId, int ibOrderId, String symbol, String exchange, String currency, String type, Double lastFillPrice, int numContracts, String status, String serviceAddress) {
        this.orderId = orderId;
        this.ibOrderId = ibOrderId;
        this.symbol = symbol;
        this.exchange = exchange;
        this.currency = currency;
        this.type = type;
        this.lastFillPrice = lastFillPrice;
        this.numContracts = numContracts;
        this.status = status;
        this.serviceAddress = serviceAddress;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getIbOrderId() {
        return ibOrderId;
    }

    public void setIbOrderId(int ibOrderId) {
        this.ibOrderId = ibOrderId;
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

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
