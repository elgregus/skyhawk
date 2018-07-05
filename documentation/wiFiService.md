WiFi Service
============

The WiFi service is used to manage the WiFi connections.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Java Methods

#### java.util.List<AccessPoint> GetAccessPoints():

The GetAccessPoints method is used to get all access points nearby.<br>
Throws StatusRuntimeException.

- param  : None
- return : 
         + java.util.List<AccessPoint> : A list of all access points nearby

#### java.util.List<Connection> GetConnections():

The GetConnections method is used to get all connections points.<br>
Throws StatusRuntimeException.

- param  : None
- return : 
         + java.util.List<Connection> : A list of all connections points nearby
		 
#### void Connect(String id):

The Connect method is used to connect to a wifi connection using it's id.<br>
Throws StatusRuntimeException.

- param  :
         + String id : String containing the id of the connection we want to connect to.
- return : None

#### void AddConnection(String id, String ssid, String psk, Boolean persistent):

The AddConnection method is used to add a connection to the connection list.<br>
Throws StatusRuntimeException.

- param  :
         + String id : String containing the id of the connection we want to add.
		 + String ssid : String containing the ssid of the connection we want to add.
		 + String psk : String containing the pre-shared key of the connection we want to add
         + Boolean id : Is the connection persistent.
- return : None

####void AutoConnect(Boolean value):
The AutoConnect method is used to tell the WiFi if we want to automatically connect to a connection when not connected.<br>
Throws StatusRuntimeException.

- param  :
         + Boolean value : Enable auto connect (true) or disable auto connect (false).
- return : None

---------------------------------

### Objects

#### AccessPoint

- Members :
          + String ssid : ssid of the access point.
		  + uint signalQuality : Quality of the signal.
-Methods : None

#### Connection

- Members :
          + String id : id of the connection.
		  + String uuid : uuid of the connection.
-Methods : None

---------------------------------

### Example

This is a short example only covering the basic operations.<br>
To see how to implement the gRPC calls directly, go see the example file BLink_WiFi.java

~~~~{.java}
package blinkDemo;

import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import blink_grpc.BLink;
import blink_grpc.Connection;
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

  // Construct client connecting to BLink server
  public BLink_WiFi_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  // Construct client for accessing GPIO service using the existing channel.
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
      blink.wifiService.AddConnection("test", "ssid", "psk134567234", false);
      blink.wifiService.Connect("brio");     
      blink.wifiService.AutoConnect(true); // optional, use to re-connect when signal is temporarily lost
      
      // Print current WiFi connections
      java.util.List<Connection> wifiConnections = blink.wifiService.GetConnections();
      for (Connection item : wifiConnections) {
          logger.info("ID: " + item.getId() + " " + item.getUuid());
      }
			
    } finally {
      blink.shutdown();
    }
  }
}

~~~~

---------------------------------

[Return to BLink services](blinkServices.md)