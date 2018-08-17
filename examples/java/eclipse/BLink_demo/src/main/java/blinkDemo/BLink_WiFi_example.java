package blinkDemo;

import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import blink_grpc.BLink;
import blink_grpc.SerialPort_Read_Reply;
import blink_grpc.SerialPort_Read_Request;
import blink_grpc.SerialPort_ServiceGrpc;
import blink_grpc.Wifi_Connect_Request;
import blink_grpc.Wifi_SetAutoConnect_Request;
import blink_grpc.SerialPort_ServiceGrpc.SerialPort_ServiceBlockingStub;
import blink_grpc.SerialPort_ServiceGrpc.SerialPort_ServiceStub;
import blink_grpc.SerialPort_Write_Request;
import blink_grpc.Wifi_GetAccessPoints_Reply.AccessPoint;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class BLink_WiFi_example {

  private final ManagedChannel channel;
  private final BLink_WiFi wifiService;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());

  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_WiFi_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing GPIO service using the existing channel. */
  public BLink_WiFi_example(ManagedChannel channel) {
    this.channel = channel;
    this.wifiService = new BLink_WiFi(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_WiFi_example blink = new BLink_WiFi_example("localhost", 50051);
    try {
      
      // WiFi Example
      java.util.List<AccessPoint> accessPoints = blink.wifiService.GetAccessPoints();
      for (AccessPoint item : accessPoints) {
        logger.info("SSID: " + item.getSsid() + " Signal: " + item.getSignalQuality() + "%");
      }
          
      // Connect to a WiFi access point
      blink.wifiService.AddSimpleConnection("test", "ssid", "psk134567234", false);
      blink.wifiService.Connect("test");     
      blink.wifiService.AutoConnect(true); // optional, use to re-connect when signal is temporarily lost
      
      // Print current WiFi connections
      java.util.List<String> wifiConnections = blink.wifiService.GetConnections();
      for (String item : wifiConnections) {
          logger.info("ID: " + item);
      }
			
    } finally {
      blink.shutdown();
    }
  }
}
