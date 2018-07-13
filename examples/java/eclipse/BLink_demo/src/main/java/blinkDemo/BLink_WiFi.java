package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;
import blink_grpc.Connection;
import blink_grpc.Wifi_AddConnection_Request;
import blink_grpc.Wifi_Connect_Request;
import blink_grpc.Wifi_GetAccessPoints_Reply;
import blink_grpc.Wifi_GetAccessPoints_Reply.AccessPoint;
import blink_grpc.Wifi_GetAccessPoints_Request;
import blink_grpc.Wifi_GetConnections_Reply;
import blink_grpc.Wifi_GetConnections_Request;
import blink_grpc.Wifi_ServiceGrpc;
import blink_grpc.Wifi_ServiceGrpc.Wifi_ServiceBlockingStub;
import blink_grpc.Wifi_SetAutoConnect_Request;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class BLink_WiFi {

    private static final Logger logger = Logger.getLogger(BLink_WiFi.class.getName());

    private final Wifi_ServiceBlockingStub blockingStub;

    public BLink_WiFi(ManagedChannel channel) {
        blockingStub = Wifi_ServiceGrpc.newBlockingStub(channel);
    }


    /** Get the WiFi access points list */
    public java.util.List<AccessPoint> GetAccessPoints() throws StatusRuntimeException {

        Wifi_GetAccessPoints_Request request = Wifi_GetAccessPoints_Request.newBuilder().build();

        Wifi_GetAccessPoints_Reply response;
        try {
            response = blockingStub.wifiGetAccessPoints(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }

        return response.getAccessPointList();
    }
    
    /** Get the WiFi connections points list */
    public java.util.List<Connection> GetConnections() throws StatusRuntimeException {

        Wifi_GetConnections_Request request = Wifi_GetConnections_Request.newBuilder().build();

        Wifi_GetConnections_Reply response;
        try {
            response = blockingStub.wifiGetConnections(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }

        return response.getWifiConnectionList();
    }

    /** Connect to a WiFi connection */
    public void Connect(String id) throws StatusRuntimeException {

        Wifi_Connect_Request request = Wifi_Connect_Request.newBuilder().setId(id).build();

        try {
            blockingStub.wifiConnect(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }

    /** Add a WiFi connection */
    public void AddConnection(String id, String ssid, String psk, Boolean persistent) throws StatusRuntimeException {

        Wifi_AddConnection_Request request = Wifi_AddConnection_Request.newBuilder().setId(id).setSsid(ssid).setPsk(psk)
                .setPersistent(persistent).build();

        try {
            blockingStub.wifiAddConnection(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }

    /** Set the auto-connect parameter */
    public void AutoConnect(Boolean value) {

        Wifi_SetAutoConnect_Request request = Wifi_SetAutoConnect_Request.newBuilder().setValue(value).build();

        try {
            blockingStub.wifiSetAutoConnect(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }
}
