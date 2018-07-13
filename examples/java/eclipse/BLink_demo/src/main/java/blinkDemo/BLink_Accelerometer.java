package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;
import blink_grpc.Accelerometer_Start_Reply;
import blink_grpc.Accelerometer_Start_Request;
import blink_grpc.Accelerometer_ServiceGrpc;
import blink_grpc.Accelerometer_ServiceGrpc.Accelerometer_ServiceBlockingStub;
import blink_grpc.Accelerometer_Stop_Request;
import blink_grpc.Accelerometer_Get_Samples_Request;
import blink_grpc.Accelerometer_Get_Samples_Reply;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.nio.ByteBuffer;


public class BLink_Accelerometer {
    private static final Logger logger = Logger.getLogger(BLink_Accelerometer.class.getName());

    private final Accelerometer_ServiceBlockingStub blockingStub;

    public BLink_Accelerometer(ManagedChannel channel) {
        blockingStub = Accelerometer_ServiceGrpc.newBlockingStub(channel);
    }

    /** Start accelerometer */
    public void start(int dataRate, int axisRange) {

        Accelerometer_Start_Request request = Accelerometer_Start_Request.newBuilder().setDataRate(dataRate).setAxisRange(axisRange).build();
        Accelerometer_Start_Reply response;

        try {
            response = blockingStub.accelerometerStart(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return;
        }

        if(response.getError().getErrorCode() != 0) {
            logger.log(Level.WARNING, "Accelerometer FAILED errno : " +
                    response.getError().getErrorCode() + " " +
                    response.getError().getErrorMessage());
        }
    }

    /** Stop accelerometer */
    public void stop() {

        Accelerometer_Stop_Request request = Accelerometer_Stop_Request.newBuilder().build();

        try {
            blockingStub.accelerometerStop(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return;
        }
    }

    /** Get samples from accelerometer */
    public java.util.List[] getSamples() {

        Accelerometer_Get_Samples_Request request = Accelerometer_Get_Samples_Request.newBuilder().build();
        Accelerometer_Get_Samples_Reply response;

        try {
            response = blockingStub.accelerometerGetSamples(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }

        if(response.getError().getErrorCode() != 0) {
            logger.log(Level.WARNING, "Accelerometer FAILED errno : " +
                    response.getError().getErrorCode() + " " +
                    response.getError().getErrorMessage());
        }

        java.util.List<Double> x = response.getXAxisList();
        java.util.List<Double> y = response.getYAxisList();
        java.util.List<Double> z = response.getZAxisList();
        return new java.util.List[] { x, y, z };
    }
}
