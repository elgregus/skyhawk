package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;

import blink_grpc.SerialPort_ServiceGrpc;
import blink_grpc.SkyHawkMezz_AnalogDiffCtrl_Request;
import blink_grpc.SkyHawkMezz_GetAnalogInputs_Reply;
import blink_grpc.SkyHawkMezz_GetAnalogInputs_Request;
import blink_grpc.SkyHawkMezz_ServiceGrpc;
import blink_grpc.SkyHawkMezz_ServiceGrpc.SkyHawkMezz_ServiceBlockingStub;
import blink_grpc.SkyHawkMezz_ServiceGrpc.SkyHawkMezz_ServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

public class BLink_SkyHawkMezz_example {
  
  private final ManagedChannel channel;
  static private SkyHawkMezz_ServiceBlockingStub blockingStub;
  private BLink_SkyHawkMezz Mezz;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkMezz_example.class.getName());
  
  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_SkyHawkMezz_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing GPIO service using the existing channel. */
  public BLink_SkyHawkMezz_example(ManagedChannel channel) {
    this.channel = channel;
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_SkyHawkMezz_example blink = new BLink_SkyHawkMezz_example("localhost", 50051);
    try {
    	blink.Mezz = new BLink_SkyHawkMezz(blink.channel);
    	blockingStub = SkyHawkMezz_ServiceGrpc.newBlockingStub(blink.channel);
    	
    	String fwVersion = blink.Mezz.getFWVersion();
    	logger.log(Level.INFO, "skyHawkLPM: fwVersion now is : " + fwVersion);
    	
    	// Activate the differential # 2
    	SkyHawkMezz_AnalogDiffCtrl_Request diffCtrlRequest = SkyHawkMezz_AnalogDiffCtrl_Request.newBuilder().setEnableDiff2(true).build();	
        try {
            blockingStub.skyHawkMezzAnalogDiffCtrl(diffCtrlRequest);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
        }

        // Fetch the analog inputs
        while(true) {
            SkyHawkMezz_GetAnalogInputs_Request request = SkyHawkMezz_GetAnalogInputs_Request.newBuilder().build();
            
            SkyHawkMezz_GetAnalogInputs_Reply response;
            try {
                response = blockingStub.skyHawkMezzGetAnalogInputs(request);
                logger.log(Level.INFO, "AnalogInputs: " + response.getAnalogInput1() + "mV "
                                                        + response.getAnalogInput2() + "mV "
                                                        + response.getAnalogInput3() + "mV "
                                                        + response.getAnalogInput4() + "mV "
                                                        + response.getAnalogDiff1() + "uA "
                                                        + response.getAnalogDiff2() + "uA");
                
            } catch (StatusRuntimeException e) {
                logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            }
            
            // Slow it down to a readable speed
            Thread.sleep(500);

        }   
          
    } finally {
      blink.shutdown();
    }
  }
}
