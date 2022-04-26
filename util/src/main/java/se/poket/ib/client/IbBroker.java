package se.poket.ib.client;

import com.ib.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.poket.microservices.api.core.order.IbOrder;
import se.poket.microservices.api.core.order.OrderService;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class IbBroker {

    private static final Logger LOG = LoggerFactory.getLogger(IbBroker.class);

    public String TWS_HOST = "192.168.1.65";
    public int TWS_PORT = 4002;

    public final IbBrokerWrapper wrapper;
    public static EClientSocket clientSocket;
    final EReaderSignal signal;
    EReader reader;
    OrderService orderService;

    private static Semaphore semaphore;
    private final Random rand = new Random();
    private int clientId = 1;
    private boolean initialized = false;

    private static final int MAX_SLEEP_MILLIS = 30000;

    public IbBroker(OrderService orderService) {
        this.orderService = orderService;
        this.wrapper = new IbBrokerWrapper(this.orderService);

        semaphore = new Semaphore(1, true);
        clientSocket = this.wrapper.getClient();
        signal = this.wrapper.getSignal();

        LOG.info("IB Host: " + TWS_HOST);
        LOG.info("IB Port: " + TWS_PORT);
    }

    public void connect() throws InterruptedException {
        clientSocket.eConnect(TWS_HOST, TWS_PORT, clientId);
        reader = new EReader(clientSocket, signal);

        reader.start();
        new Thread(() -> {
            while (clientSocket.isConnected()) {
                signal.waitForSignal();
                try {
                    reader.processMsgs();
                } catch (Exception e) {
                    LOG.error("Exception: " + e.getMessage());
                }
            }
        }).start();
        Thread.sleep(1000);
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
        LOG.info("IB Client ID: " + this.clientId);
    }

    public IbOrder buyOrder(IbOrder buyOrder) {
        LOG.info("starting buyOrder");
        int numContracts = buyOrder.getNumContracts();
        if (numContracts > 0) {
            LOG.info("preparing to buy " + numContracts + " shares" + " of " + buyOrder.getSymbol());
            Order order = getOrder(1, numContracts);
            Contract contract = getContract(buyOrder);
            buyOrder.setIbOrderId(order.orderId());
            buyOrder.setSentDate(new Date());
            buyOrder.setLastUpdateDate(new Date());
            buyOrder.setStatus("InProgress");
            this.orderService.updateOrder(buyOrder);
            clientSocket.placeOrder(order.orderId(), contract, order);
            LOG.info("placed buy order for " + numContracts + " of " + buyOrder.getSymbol());
        } else {
            LOG.info("buy order not placed (0 contracts requested)");
        }
        return buyOrder;
    }

    public int getCurrentOrderId() {
        return wrapper.getCurrentOrderId();
    }

    public void setOrderId(int orderId) {
        wrapper.nextValidId(orderId);
    }

    public Order getOrder(int actionIndex, int numShares) {
        Order order = new Order();
        order.clientId(clientId);
        order.orderType("MKT");
        if (actionIndex == 1) {
            order.action("BUY");
        } else if (actionIndex == 2) {
            order.action("SELL");
        }

        LOG.info("new order id: " + getCurrentOrderId());
        order.orderId(getCurrentOrderId());
        int permId = Math.abs(rand.nextInt());
        LOG.info("new order perm id: " + permId);
        order.permId(permId);

        order.totalQuantity(Decimal.get(numShares));
        order.outsideRth(true);
        order.tif("GTC");
        order.lmtPrice(0.0);
        order.auxPrice(0.0);

        return order;
    }

    public Contract getContract(IbOrder order) {
        Contract contract = new Contract();
        contract.exchange(order.getExchange());
        if (!order.getExchange().equals("SMART")) {
            contract.primaryExch(order.getExchange());
        }

        contract.localSymbol(order.getSymbol());
        contract.currency(order.getCurrency());
        contract.secType(order.getType());
        LOG.info("symbol: " + contract.localSymbol());
        LOG.info("exchange: " + contract.exchange());
        LOG.info("primary exchange: " + contract.primaryExch());
        LOG.info("currency: " + contract.currency());

        return contract;
    }

    public void initialize() {
        clientSocket.reqIds(-1);
        clientSocket.reqAllOpenOrders();
        initialized = true;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public void wrapUp() {
        clientSocket.reqAllOpenOrders();
        clientSocket.eDisconnect();
    }

    public static EClientSocket getClientSocket() {
        return clientSocket;
    }
}
