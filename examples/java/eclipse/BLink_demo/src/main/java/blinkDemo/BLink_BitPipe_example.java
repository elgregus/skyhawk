package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import blink_grpc.BLink;
import blink_grpc.BitPipe_ServiceGrpc.BitPipe_ServiceBlockingStub;
import blink_grpc.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;

public class BLink_BitPipe_example {

  private final ManagedChannel channel; 
  private BitPipe_ServiceBlockingStub bitPipeStub;

  private static final Logger logger = Logger.getLogger(BLink_BitPipe_example.class.getName());

  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_BitPipe_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing the BitPipe service using the existing channel. */
  public BLink_BitPipe_example(ManagedChannel channel) {
    this.channel = channel;
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_BitPipe_example blink = new BLink_BitPipe_example("localhost", 50051);
    try {
      //Init stub
      blink.bitPipeStub = BitPipe_ServiceGrpc.newBlockingStub(blink.channel);    
      
      // Disconnect the modem
      BitPipe_Disconnect_Request disconnect_Request = BitPipe_Disconnect_Request.newBuilder().build();
      blink.bitPipeStub.bitPipeDisconnect(disconnect_Request);

      Thread.sleep(5000); // Wait a bit
      
      // Set the APN
      BitPipe_SetAPN_Request setAPN_Request = BitPipe_SetAPN_Request.newBuilder().setApn("m2minternet.apn").build();
      blink.bitPipeStub.bitPipeSetAPN(setAPN_Request);

      // Connect the BitPipe to the cellular network
      BitPipe_Connect_Request connect_Request = BitPipe_Connect_Request.newBuilder().build();
      blink.bitPipeStub.bitPipeConnect(connect_Request);

      // Check the currently configured APN
      BitPipe_GetAPN_Request getAPN_Request = BitPipe_GetAPN_Request.newBuilder().build();
      BitPipe_GetAPN_Reply getAPN_Reply = blink.bitPipeStub.bitPipeGetAPN(getAPN_Request);
      System.out.println("Currently configured APN is: " + getAPN_Reply.getApn());

      // Wait for the Bitpipe to be connected
      Gson gson = new Gson();
      Type type = new TypeToken<Map<String, String>>(){}.getType();

      Map<String, String> bitpipeStatus;

      do {
        BitPipe_GetStatus_Request getStatus_Request = BitPipe_GetStatus_Request.newBuilder().build();
        BitPipe_GetStatus_Reply getStatus_Reply = blink.bitPipeStub.bitPipeGetStatus(getStatus_Request);
        bitpipeStatus = gson.fromJson(getStatus_Reply.getStatusJson(), type);
        System.out.println("Waiting for the modem to connect... Current status is: " + bitpipeStatus.get("modem_state"));
        Thread.sleep(1000);
      } while(!bitpipeStatus.get("modem_state").equals("CONNECTED"));

      // Print the IMEI
      System.out.println("The modem IMEI is: " + bitpipeStatus.get("imei"));

      // Check the current signal RSSI in dBm
      BitPipe_GetSignalInfo_Request getSignal_Request = BitPipe_GetSignalInfo_Request.newBuilder().build();
      BitPipe_GetSignalInfo_Reply getSignal_Reply = blink.bitPipeStub.bitPipeGetSignalInfo(getSignal_Request);
      Map<String, String> bitpipeSignal = gson.fromJson(getSignal_Reply.getSignalInfoJson(), type);
      System.out.println("Current signal RRSI is : " + bitpipeSignal.get("rssi"));

    } finally {
      blink.shutdown();
    }
  }
}
