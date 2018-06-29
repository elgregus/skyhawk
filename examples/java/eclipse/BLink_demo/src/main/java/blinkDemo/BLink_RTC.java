package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;

import blink_grpc.RTC_GetTime_Reply;
import blink_grpc.RTC_GetTime_Request;
import blink_grpc.RTC_SetTime_Reply;
import blink_grpc.RTC_SetTime_Request;
import blink_grpc.RTC_ServiceGrpc;
import blink_grpc.RTC_ServiceGrpc.RTC_ServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;


public class BLink_RTC {


	private static final Logger logger = Logger.getLogger(BLink_GPIO.class.getName());

    private final RTC_ServiceBlockingStub blockingStub;

    public BLink_RTC(ManagedChannel channel) {
        blockingStub = RTC_ServiceGrpc.newBlockingStub(channel);
    }

    /** Get the RTC time (unixTimestamp) */
    public long GetTime() {

    	RTC_GetTime_Request request = RTC_GetTime_Request.newBuilder().build();

    	RTC_GetTime_Reply reply;
        try {
        	reply = blockingStub.rTCGetTime(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return -1;
        }
        
        return reply.getUnixTimestamp();
    }
    
    /** Set the RTC time (unixTimestamp) */
    public void SetTime(long time) {

    	RTC_SetTime_Request request = RTC_SetTime_Request.newBuilder().setUnixTimestamp(time).build();

        try {
        	blockingStub.rTCSetTime(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return;
        }
    }
}
