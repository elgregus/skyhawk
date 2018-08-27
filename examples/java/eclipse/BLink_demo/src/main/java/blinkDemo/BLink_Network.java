package blinkDemo;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import blink_grpc.Network_AddConnection_Request;
import blink_grpc.Network_GetConnections_Reply;
import blink_grpc.Network_GetConnections_Request;
import blink_grpc.Network_GetState_Reply;
import blink_grpc.Network_GetState_Request;
import blink_grpc.Network_RemoveConnection_Reply;
import blink_grpc.Network_RemoveConnection_Request;
import blink_grpc.Network_ServiceGrpc;
import blink_grpc.Network_ServiceGrpc.Network_ServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;



public class BLink_Network {


	private static final Logger logger = Logger.getLogger(BLink_GPIO.class.getName());

    private final Network_ServiceBlockingStub blockingStub;

    public BLink_Network(ManagedChannel channel) {
        blockingStub = Network_ServiceGrpc.newBlockingStub(channel);
    }

    /** Get the network state value */
    public int GetState() {

        Network_GetState_Request request = Network_GetState_Request.newBuilder().build();

        Network_GetState_Reply reply;
        try {
        	reply = blockingStub.networkGetState(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return -1;
        }
        
        return reply.getNetworkState();
    }
    
    public void AddConnection(JsonObject settings, Boolean saveToDisk) {
        Network_AddConnection_Request request = Network_AddConnection_Request.newBuilder().setJsonSettings(settings.toString()).setSaveToDisk(saveToDisk).build();

        try {
            blockingStub.networkAddConnection(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return;
        }
    }
    
    public Boolean RemoveConnection(String connectionId) {
        Network_RemoveConnection_Request request = Network_RemoveConnection_Request.newBuilder().setConnectionId(connectionId).build();

        Network_RemoveConnection_Reply response;
        try {
            response = blockingStub.networkRemoveConnection(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return false;
        }
        
        return response.getConnectionRemoved();
    }

    public java.util.List<String> GetConnections() throws StatusRuntimeException {

        Network_GetConnections_Request request = Network_GetConnections_Request.newBuilder().build();

        Network_GetConnections_Reply response;
        try {
            response = blockingStub.networkGetConnections(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }

        return response.getConnectionIdList(); 
    }
}
