package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import blink_grpc.Gpio_Get_Reply;
import blink_grpc.Gpio_Get_Request;
import blink_grpc.Gpio_ServiceGrpc;
import blink_grpc.Gpio_ServiceGrpc.Gpio_ServiceBlockingStub;
import blink_grpc.Gpio_ServiceGrpc.Gpio_ServiceStub;
import blink_grpc.Gpio_Set_Request;
import blink_grpc.Gpio_Set_Reply;
import blink_grpc.Gpio_StartStreaming_Request;
import blink_grpc.Gpio_StartStreaming_Reply;
import blink_grpc.Gpio_StopStreaming_Request;
import blink_grpc.Gpio_StopStreaming_Reply;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;


public class BLink_GPIO {
    private static final Logger logger = Logger.getLogger(BLink_GPIO.class.getName());

    private Gpio_ServiceStub streamStub;
    private final Gpio_ServiceBlockingStub blockingStub;

    public BLink_GPIO(ManagedChannel channel) {
        blockingStub = Gpio_ServiceGrpc.newBlockingStub(channel);
        streamStub = Gpio_ServiceGrpc.newStub(channel);
    }

    /** Set the GPIO value */
    public void setGPIO(String name, Boolean value) {

        Gpio_Set_Request request = Gpio_Set_Request.newBuilder().setName(name).setRequestedState(value).build();

        Gpio_Set_Reply response;
        
        try {
        	response = blockingStub.gpioSet(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus().getDescription() + " " + e.getMessage());

            // Get the description
            JsonParser parser = new JsonParser();
            JsonObject jsonDescription = parser.parse(e.getStatus().getDescription()).getAsJsonObject();
            
            //Convert to int and string
            int errorCode = jsonDescription.get("errorCode").getAsInt();
            String errorMessage = jsonDescription.get("errorMessage").getAsString();
            
            System.out.println(Integer.toString(errorCode) + " : " + errorMessage);
            return;
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

        return response.getValue();
    }

    /** Stream GPIO value */
    public void StartStreamGPIO(String name, String edge) {

        Gpio_StartStreaming_Request request = Gpio_StartStreaming_Request.newBuilder().setName(name).setEdge(edge).build();
        streamStub.gpioStartStreaming(request, new StreamObserver<blink_grpc.Gpio_StartStreaming_Reply>() {

            public void onNext(Gpio_StartStreaming_Reply reply) {
                Boolean received = reply.getValue();
                logger.log(Level.INFO, "Stream value: " + received);
            }

            public void onError(Throwable e) {
                // TODO Error management
                logger.log(Level.INFO, "Stream error!");
                logger.log(Level.INFO, e.getMessage());
            }

            public void onCompleted() {
                logger.log(Level.INFO, "Stream completed!");
                System.exit(0);
            }
        });
    }

    public void StopStreamGPIO(String name) {
        Gpio_StopStreaming_Request request = Gpio_StopStreaming_Request.newBuilder().setName(name).build();

        Gpio_StopStreaming_Reply response;
        try {
            response = blockingStub.gpioStopStreaming(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }
}
