package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;
import blink_grpc.SkyHawkMezz_UpdateFW_Request;
import blink_grpc.SkyHawkMezz_UpdateFW_Reply;
import blink_grpc.SkyHawkMezz_GetFWVersion_Request;
import blink_grpc.SkyHawkMezz_GetFWVersion_Reply;
import blink_grpc.SkyHawkMezz_GetHWVersion_Request;
import blink_grpc.SkyHawkMezz_GetHWVersion_Reply;
import blink_grpc.SkyHawkMezz_ServiceGrpc;
import blink_grpc.SkyHawkMezz_ServiceGrpc.SkyHawkMezz_ServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class BLink_SkyHawkMezz {

	private static final Logger logger = Logger.getLogger(BLink_SkyHawkMezz.class.getName());

	private final SkyHawkMezz_ServiceBlockingStub blockingStub;
	  
	public BLink_SkyHawkMezz(ManagedChannel channel) {
		blockingStub = SkyHawkMezz_ServiceGrpc.newBlockingStub(channel);
	}

    
    /** Update the Firmware */
    public Boolean updateFW(String filePath) {
    
    	SkyHawkMezz_UpdateFW_Request request = SkyHawkMezz_UpdateFW_Request.newBuilder().setFilePath(filePath).build();
    	
    	SkyHawkMezz_UpdateFW_Reply response;
        try {
            response = blockingStub.skyHawkMezzUpdateFW(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return false;
        }
        
        return response.getSuccess();
    }
    
    /** Get the firmware verion */
    public String getFWVersion() {

    	SkyHawkMezz_GetFWVersion_Request request = SkyHawkMezz_GetFWVersion_Request.newBuilder().build();
    	
    	SkyHawkMezz_GetFWVersion_Reply response;
        try {
            response = blockingStub.skyHawkMezzGetFWVersion(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return "";
        }
        
        return response.getFwVersion();
    }
    
    /** Get the Hardware verion */
    public int getHWVersion() {

    	SkyHawkMezz_GetHWVersion_Request request = SkyHawkMezz_GetHWVersion_Request.newBuilder().build();

    	SkyHawkMezz_GetHWVersion_Reply response;
        try {
            response = blockingStub.skyHawkMezzGetHWVersion(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return -1;
        }
        
        return response.getHwVersion();
    }
}
