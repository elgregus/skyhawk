Wiegand Service
============

The wiegand Service is used to read the wiegand port on the Mezz of the device. This port is read only and all configuration is handled by the server.

Important note : The Wiegand port is plexed with the OneWire port. You need to [use the SkyHawkMisc Service](skyHawkMiscService.md) to select which mode you want to use.

The methods described and the example below will make direct use of gRPC calls.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Java Methods

Since gRPC uses a system of request and reply, paramters in the request object have been tagged as param and return values in the reply object have been tagged as return values.

#### void Wiegand_Read():

The Read method is used to read on the wiegand port of the device.
It will create a stream of data that can be accessed in the : "onNext" callback.
You only need to provide the name of the device in the Request object
In the callback, you will need to get the gRPC reply, get your value (as a long).
See at the end of this page for a complete example.

- param  :
         + String deviceName : Name of the wiegand device we want to read from.
- return : None

#### void Serial_StopReading():

Sends a command to stop a stream of reading on a specified wiegand device.

- param  :
         + String devince Name : Name of the device we want to stop reading from.
- return : None


---------------------------------

---------------------------------

### Java Example

This is a short example only covering the basic operations.<br>
To run this example, plug a wiegand device on your target and execute BLink_Wiegand_example.jar-jar-with-dependencies.jar.
Every sent from the wiegand device will show in the console.
This example executes for 60 seconds, and then stops the streaming and exits.

~~~~{.java}
package blinkDemo;

import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import blink_grpc.BLink;
import blink_grpc.Wiegand_Read_Reply;
import blink_grpc.Wiegand_Read_Request;
import blink_grpc.Wiegand_StopReading_Request;
import blink_grpc.Wiegand_ServiceGrpc;
import blink_grpc.Wiegand_ServiceGrpc.Wiegand_ServiceBlockingStub;
import blink_grpc.Wiegand_ServiceGrpc.Wiegand_ServiceStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class BLink_Wiegand_example {
  
  private final ManagedChannel channel;
  private Wiegand_ServiceStub wiegandReadStub;
  private Wiegand_ServiceBlockingStub wiegandWriteStub;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());

  // Construct client connecting to BLink server
  public BLink_Wiegand_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  // Construct client for accessing Wiegand service using the existing channel.
  public BLink_Wiegand_example(ManagedChannel channel) {
    this.channel = channel;
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_Wiegand_example blink = new BLink_Wiegand_example("localhost", 50051);
    try {
    
      // Serial Port Example (Echo of RX:ttymxc1 into TX:ttymxc1)
      blink.wiegandReadStub = Wiegand_ServiceGrpc.newStub(blink.channel);
      blink.wiegandWriteStub = Wiegand_ServiceGrpc.newBlockingStub(blink.channel);     
      Wiegand_Read_Request readRequest = Wiegand_Read_Request.newBuilder().setDeviceName("MezzWiegand").build();
      
      blink.wiegandReadStub.wiegandRead(readRequest, new StreamObserver<blink_grpc.Wiegand_Read_Reply>() {
		public void onNext(Wiegand_Read_Reply value) {
			// Print the received value
			System.out.println("New long : " + Long.toString(value.getData())); 
		}

		public void onError(Throwable t) {
			// TODO Auto-generated method stub
		}

		public void onCompleted() {
			System.out.println("COMPLETED");
			System.exit(0);
			// TODO Auto-generated method stub
		}
		
      });

      while(true){
    	// Stop reading wiegand after 60 seconds
        Thread.sleep(60000);
        Wiegand_StopReading_Request stopReadingRequest = Wiegand_StopReading_Request.newBuilder()
                .setDeviceName("MezzWiegand")
                .build();
        blink.wiegandWriteStub.wiegandStopReading(stopReadingRequest);
      }

			
    } finally {
      blink.shutdown();
    }
  }
}


~~~~

---------------------------------

[Return to BLink services](blinkServices.md)
