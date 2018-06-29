package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;
import blink_grpc.Gpio_Get_Reply;
import blink_grpc.Gpio_Get_Request;
import blink_grpc.Gpio_ServiceGrpc;
import blink_grpc.Gpio_ServiceGrpc.Gpio_ServiceBlockingStub;
import blink_grpc.Gpio_Set_Request;
import blink_grpc.Gpio_Set_Reply;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;


public class BLink_GPIO {
    private static final Logger logger = Logger.getLogger(BLink_GPIO.class.getName());

    private final Gpio_ServiceBlockingStub blockingStub;

    public BLink_GPIO(ManagedChannel channel) {
        blockingStub = Gpio_ServiceGrpc.newBlockingStub(channel);
    }

    /** Set the GPIO value */
    public void setGPIO(String name, Boolean value) {

        Gpio_Set_Request request = Gpio_Set_Request.newBuilder().setName(name).setRequestedState(value).build();

        Gpio_Set_Reply response;
        
        try {
        	response = blockingStub.gpioSet(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return;
        }
        
        if(response.getError().getErrorCode() != 0) {
        	logger.log(Level.WARNING, "GPIO FAILED errno : " + 
                    response.getError().getErrorCode() + " " + 
        			response.getError().getErrorMessage());
        }
    }

    /** Get the GPIO value */
    public Boolean getGPIO(String name) {

        Gpio_Get_Request request = Gpio_Get_Request.newBuilder().setName(name).build();

        Gpio_Get_Reply response;
        try {
            response = blockingStub.gpioGet(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
        
        if(response.getError().getErrorCode() != 0) {
        	logger.log(Level.WARNING, "GPIO FAILED errno : " + 
                    response.getError().getErrorCode() + " " + 
        			response.getError().getErrorMessage());
        }
        
        return response.getValue();
    }

}
