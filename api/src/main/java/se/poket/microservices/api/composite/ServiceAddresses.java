package se.poket.microservices.api.composite;

public class ServiceAddresses {
    private final String cmp;
    private final String ord;

    public ServiceAddresses() {
        cmp = null;
        ord = null;
    }

    public ServiceAddresses(
            String compositeAddress,
            String orderAddress) {

        this.cmp = compositeAddress;
        this.ord = orderAddress;
    }

    public String getCmp() {
        return cmp;
    }

    public String getOrd() {
        return ord;
    }
}
