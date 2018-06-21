package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;

import blink_grpc.Network_GetState_Reply;
import blink_grpc.Network_GetState_Request;
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
}
