package se.poket.microservices.core.order.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "iborders")
public class IbOrderEntity {

    @Id
    private String id;
    @Version
    private Integer version;
    @Indexed(unique = true)
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

    public IbOrderEntity() {

    }

    public IbOrderEntity(int orderId, int ibOrderId, String symbol, String exchange, String currency, String type, Double lastFillPrice, int numContracts, String status, Date createdDate, Date sentDate, Date lastUpdateDate) {
        this.orderId = orderId;
        this.ibOrderId = ibOrderId;
        this.symbol = symbol;
        this.exchange = exchange;
        this.currency = currency;
        this.type = type;
        this.lastFillPrice = lastFillPrice;
        this.numContracts = numContracts;
        this.createdDate = createdDate;
        this.sentDate = sentDate;
        this.lastUpdateDate = lastUpdateDate;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int oderId) {
        this.orderId = oderId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
