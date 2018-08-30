package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import blink_grpc.BLink;
import blink_grpc.WiegandEN_OneWireDIS_Set_Reply;
import blink_grpc.WiegandEN_OneWireDIS_Set_Request;
import blink_grpc.WiegandEN_OneWireDIS_Get_Request;
import blink_grpc.WiegandEN_OneWireDIS_Get_Reply;
import blink_grpc.WWANAntennaEN_Set_Reply;
import blink_grpc.WWANAntennaEN_Set_Request;
import blink_grpc.WWANAntennaEN_Get_Request;
import blink_grpc.WWANAntennaEN_Get_Reply;
import blink_grpc.PoEEN_Set_Reply;
import blink_grpc.PoEEN_Set_Request;
import blink_grpc.PoEEN_Get_Request;
import blink_grpc.PoEEN_Get_Reply;
import blink_grpc.MezzSerialMode_Set_Reply;
import blink_grpc.MezzSerialMode_Set_Request;
import blink_grpc.MezzSerialMode_Get_Request;
import blink_grpc.MezzSerialMode_Get_Reply;
import blink_grpc.DetectMezz_Request;
import blink_grpc.DetectMezz_Reply;
import blink_grpc.SkyHawkMisc_ServiceGrpc;
import blink_grpc.SkyHawkMisc_ServiceGrpc.SkyHawkMisc_ServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_SkyHawkMisc_example {

  private final ManagedChannel channel; 
  private SkyHawkMisc_ServiceBlockingStub skyhawkMiscStub;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());

  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_SkyHawkMisc_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing GPIO service using the existing channel. */
  public BLink_SkyHawkMisc_example(ManagedChannel channel) {
    this.channel = channel;
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_SkyHawkMisc_example blink = new BLink_SkyHawkMisc_example("localhost", 50051);
    try {
      //Init stub
      blink.skyhawkMiscStub = SkyHawkMisc_ServiceGrpc.newBlockingStub(blink.channel);    

      // Detect Mezz
      DetectMezz_Request detectMezzRequest = DetectMezz_Request.newBuilder().build();
      DetectMezz_Reply detectMezzResponse = blink.skyhawkMiscStub.detectMezz(detectMezzRequest);
      
      // Enable Wiegand, Disable PoE, Set Mezz to RS232.
      if (!detectMezzResponse.getValue()) {
    	MezzSerialMode_Get_Request getSerialRequest = MezzSerialMode_Get_Request.newBuilder().build();
    	MezzSerialMode_Get_Reply getSerialReply = blink.skyhawkMiscStub.mezzSerialModeGet(getSerialRequest);
    	System.out.println(getSerialReply.getValue());
    	
    	WiegandEN_OneWireDIS_Set_Request setWiegRequest = WiegandEN_OneWireDIS_Set_Request.newBuilder().setRequestedState(true).build();
    	blink.skyhawkMiscStub.wiegandENOneWireDISSet(setWiegRequest);
    	PoEEN_Set_Request setPoERequest = PoEEN_Set_Request.newBuilder().setRequestedState(false).build();
    	blink.skyhawkMiscStub.poEENSet(setPoERequest);
    	MezzSerialMode_Set_Request setSerialRequest = MezzSerialMode_Set_Request.newBuilder().setMode("RS232").build();
    	blink.skyhawkMiscStub.mezzSerialModeSet(setSerialRequest);
    	getSerialRequest = MezzSerialMode_Get_Request.newBuilder().build();
    	getSerialReply = blink.skyhawkMiscStub.mezzSerialModeGet(getSerialRequest);
    	System.out.println(getSerialReply.getValue());
      }
    } finally {
      blink.shutdown();
    }
  }
}
