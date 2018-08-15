package blinkDemo;

import java.util.logging.Level;
import java.util.logging.Logger;

import blink_grpc.Accelerometer_ServiceGrpc;
import blink_grpc.Accelerometer_ServiceGrpc.Accelerometer_ServiceBlockingStub;
import blink_grpc.Accelerometer_Get_Samples_Request;
import blink_grpc.Accelerometer_Get_Samples_Reply;
import blink_grpc.Accelerometer_Set_Data_Rate_Request;
import blink_grpc.Accelerometer_Set_Range_Request;
import blink_grpc.Accelerometer_Set_Trigger_Request;
import blink_grpc.Accelerometer_Start_Monitoring_Request;
import blink_grpc.Accelerometer_Start_Trigger_Request;
import blink_grpc.Accelerometer_Stop_Monitoring_Request;
import blink_grpc.Accelerometer_Stop_Trigger_Request;

import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;

import java.nio.ByteBuffer;


public class BLink_Accelerometer {
    private static final Logger logger = Logger.getLogger(BLink_Accelerometer.class.getName());

    private final Accelerometer_ServiceBlockingStub blockingStub;

    public BLink_Accelerometer(ManagedChannel channel) {
        blockingStub = Accelerometer_ServiceGrpc.newBlockingStub(channel);
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

        java.util.List<Double> x = response.getXAxisList();
        java.util.List<Double> y = response.getYAxisList();
        java.util.List<Double> z = response.getZAxisList();
        java.util.List<Long> timestamp = response.getTimestampList();
        return new java.util.List[] { x, y, z, timestamp };
    }

    /** Set output data rate */
    public void setDataRate(int dataRate) {
        Accelerometer_Set_Data_Rate_Request request = Accelerometer_Set_Data_Rate_Request.newBuilder().setDataRate(dataRate).build();

        try {
            blockingStub.accelerometerSetDataRate(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }

    /** Set range */
    public void setRange(int axisRange) {
        Accelerometer_Set_Range_Request request = Accelerometer_Set_Range_Request.newBuilder().setAxisRange(axisRange).build();

        try {
            blockingStub.accelerometerSetRange(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }

    /** Set trigger */
    public void setTrigger(int threshold_x, int threshold_y, int threshold_z, int duration, int configuration) {
        Accelerometer_Set_Trigger_Request request = Accelerometer_Set_Trigger_Request.newBuilder().setThsX(threshold_x).setThsY(threshold_y).setThsZ(threshold_z).setDur(duration).setConf(configuration).build();

        try {
            blockingStub.accelerometerSetTrigger(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }

    /** Start monitoring */
    public void startMonitoring() {
        Accelerometer_Start_Monitoring_Request request = Accelerometer_Start_Monitoring_Request.newBuilder().build();

        try {
            blockingStub.accelerometerStartMonitoring(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }

    /** Start trigger */
    public void startTrigger() {
        Accelerometer_Start_Trigger_Request request = Accelerometer_Start_Trigger_Request.newBuilder().build();

        try {
            blockingStub.accelerometerStartTrigger(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }

    /** Stop monitoring */
    public void stopMonitoring() {
        Accelerometer_Stop_Monitoring_Request request = Accelerometer_Stop_Monitoring_Request.newBuilder().build();

        try {
            blockingStub.accelerometerStopMonitoring(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }

    /** Stop trigger */
    public void stopTrigger() {
        Accelerometer_Stop_Trigger_Request request = Accelerometer_Stop_Trigger_Request.newBuilder().build();

        try {
            blockingStub.accelerometerStopTrigger(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
    }
}
