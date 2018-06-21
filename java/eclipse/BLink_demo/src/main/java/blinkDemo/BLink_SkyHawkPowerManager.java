package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;
import blink_grpc.SkyHawkPmgr_GetRegister_Request;
import blink_grpc.SkyHawkPmgr_GetRegister_Reply;
import blink_grpc.SkyHawkPmgr_SetRegister_Request;
import blink_grpc.SkyHawkPmgr_SetRegister_Reply;
import blink_grpc.SkyHawkPmgr_UpdateFW_Request;
import blink_grpc.SkyHawkPmgr_UpdateFW_Reply;
import blink_grpc.SkyHawkPmgr_GetFWVersion_Request;
import blink_grpc.SkyHawkPmgr_GetFWVersion_Reply;
import blink_grpc.SkyHawkPmgr_GetHWVersion_Request;
import blink_grpc.SkyHawkPmgr_GetHWVersion_Reply;
import blink_grpc.SkyHawkPowerManager_ServiceGrpc;
import blink_grpc.SkyHawkPowerManager_ServiceGrpc.SkyHawkPowerManager_ServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

public class BLink_SkyHawkPowerManager {

	private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager.class.getName());

	private final SkyHawkPowerManager_ServiceBlockingStub blockingStub;
	  
	public BLink_SkyHawkPowerManager(ManagedChannel channel) {
		blockingStub = SkyHawkPowerManager_ServiceGrpc.newBlockingStub(channel);
	}

    /** Set the register value */
    public void setRegister(String regName, int value) {

    	SkyHawkPmgr_SetRegister_Request request = SkyHawkPmgr_SetRegister_Request.newBuilder().setRegName(regName).setValue(value).build();

        try {
            blockingStub.skyHawkPmgrSetRegister(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return;
        }
    }

    /** Get the register value */
    public int getRegister(String regName) {

    	SkyHawkPmgr_GetRegister_Request request = SkyHawkPmgr_GetRegister_Request.newBuilder().setRegName(regName).build();

    	SkyHawkPmgr_GetRegister_Reply response;
        try {
            response = blockingStub.skyHawkPmgrGetRegister(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return -1;
        }
        
        return response.getValue();
    }
    
    /** Update the Firmware */
    public Boolean updateFW(String filePath) {
    
    	SkyHawkPmgr_UpdateFW_Request request = SkyHawkPmgr_UpdateFW_Request.newBuilder().setFilePath(filePath).build();
    	
    	SkyHawkPmgr_UpdateFW_Reply response;
        try {
            response = blockingStub.skyHawkPmgrUpdateFW(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return false;
        }
        
        return response.getSuccess();
    }
    
    /** Get the firmware verion */
    public String getFWVersion() {

    	SkyHawkPmgr_GetFWVersion_Request request = SkyHawkPmgr_GetFWVersion_Request.newBuilder().build();
    	
    	SkyHawkPmgr_GetFWVersion_Reply response;
        try {
            response = blockingStub.skyHawkPmgrGetFWVersion(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return "";
        }
        
        return response.getFwVersion();
    }
    
    /** Get the Hardware verion */
    public int getHWVersion() {

    	SkyHawkPmgr_GetHWVersion_Request request = SkyHawkPmgr_GetHWVersion_Request.newBuilder().build();

    	SkyHawkPmgr_GetHWVersion_Reply response;
        try {
            response = blockingStub.skyHawkPmgrGetHWVersion(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return -1;
        }
        
        return response.getHwVersion();
    }
}
