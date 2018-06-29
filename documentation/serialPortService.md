Serial Port Service
============

The Serial Port Service is used communicate with the serial port on the device. you may write or read on this serial port. Configuration is optional, since the server automatically does a default configuration.

The methods described and the example below will make direct use of gRPC calls.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Skyhawk Capabilities
Here is a list of capabilites for each configuration field

Field          | Type    | Values                                      
---------------|-------- | --------------------------------------------
Name           | String  | "BaseSerial" : DB9 connector <br> "MezzSerial" : Mezz connector                              
Baudrate       | int     | Any int                                      
Parity         | String  | "none", "odd", "even"                        
Character Size | int     | 5 to 9
Flow Control   | String  | "none", "software", "hardware"
Stop Bits      | String  | "one", "onepointfive", "two"

#### Default Configurations

**Baudrate :** 115200
**Parity :** none
**Character size :** 8
**Flow control :** none
**Stop bits :** one

---------------------------------

### Java Methods

Since gRPC always uses a system of request and reply, to simplify this documentation paramters in the request object have simply been tagged as param and return values in the reply object have been tagged as return values.

#### uint64 Serial_Read():

The Read method is used to read on the serial port of the device.
It will create a stream of data that can be accessed in the : "onNext" callback.
You only need to provide the name of the device in the Request object
In the callback, you will need to get the gRPC reply, get your value and convert it to UTF-8 string.
See at the end of this page for a complete example.

- param  :
         + String deviceName : Name of the device we want to read from.
- return : None
		 
#### void Serial_Write(String deviceName, bytes data):

Writes the bytes to the requested serial device. Bytes must be in UTF-8.

- param  : 
         + String deviceName : Name of the device we want to write to.
		 + bytes data : All the bytes that we want to write.
- return : None

#### void Serial_Config(String deviceName, int baudrate, String parity, int charSize, String stopBits, String flowControl)

The Config method is used to configure a serial port on the device.

- param : 
        + String deviceName : Name of the device we want to configure.
		+ int baudrate : Baudrate of the device we want to configure.
		+ String parity : Parity of the device we want to configure.
		+ int charSize : Character size of the device we want to configure.
		+ String stopBits : Stop bit of the device we want to configure.
		+ String flowControl :
- return : None

#### void Serial_StopReading():

Sends a command to stop a stream of reading on a specified serial device.

- param  :
         + String devince Name : Name of the device we want to stop reading from.
- return : None


---------------------------------

### Java Example

This is a short example only covering the basic operations.<br>
To run this example, plug a RS232 cable on your target and execute BLink_SerialPort_example.jar-jar-with-dependencies.jar.
Then open a putty in serial with the default configuration detailed above.
Every character you send with putty will be echoed back to you, also if you press "c" the config method will be called and the baudrate will change to 9600 (relaunch putty with a configuration at 9600 baud to continue)
If you press "q" the stopReading method will be called and the target will stop sending the char stream to your .jar (you will lose your echo in putty)

~~~~{.java}
package blinkDemo;

import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import blink_grpc.BLink;
import blink_grpc.SerialPort_Read_Reply;
import blink_grpc.SerialPort_Read_Request;
import blink_grpc.SerialPort_Config_Request;
import blink_grpc.SerialPort_Config_Reply;
import blink_grpc.SerialPort_StopReading_Request;
import blink_grpc.SerialPort_ServiceGrpc;
import blink_grpc.SerialPort_ServiceGrpc.SerialPort_ServiceBlockingStub;
import blink_grpc.SerialPort_ServiceGrpc.SerialPort_ServiceStub;
import blink_grpc.SerialPort_Write_Request;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class BLink_SerialPort_example {
  
  private final ManagedChannel channel;
  private SerialPort_ServiceStub serialReadStub;
  private SerialPort_ServiceBlockingStub serialWriteStub;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());

  // Construct client connecting to BLink server
  public BLink_SerialPort_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  // Construct client for accessing serialPort service using the existing channel. 
  public BLink_SerialPort_example(ManagedChannel channel) {
    this.channel = channel;
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_SerialPort_example blink = new BLink_SerialPort_example("localhost", 50051);
    try {
    
      // Serial Port Example (Echo of RX:ttymxc1 into TX:ttymxc1)
      blink.serialReadStub = SerialPort_ServiceGrpc.newStub(blink.channel);
      blink.serialWriteStub = SerialPort_ServiceGrpc.newBlockingStub(blink.channel);     
      SerialPort_Read_Request readRequest = SerialPort_Read_Request.newBuilder().setDeviceName("BaseSerial").build();
      
      blink.serialReadStub.serialPortRead(readRequest, new StreamObserver<blink_grpc.SerialPort_Read_Reply>() {
		public void onNext(SerialPort_Read_Reply value) {
			// Change config on c press
			System.out.println("New char : " + value.getData().toStringUtf8());
			if(value.getData().toStringUtf8().equals("c")) {
				SerialPort_Config_Request configRequest = SerialPort_Config_Request.newBuilder().setDeviceName("BaseSerial")
						.setBaudrate(9600)
						.setCharSize(8)
						.setFlowControl("none")
						.setParity("none")
						.setStopBits("one")
						.build();
				blink.serialWriteStub.serialPortConfig(configRequest);
			} else if(value.getData().toStringUtf8().equals("q")) {
				SerialPort_StopReading_Request stopReadingRequest = SerialPort_StopReading_Request.newBuilder().setDeviceName("BaseSerial")
					.build();
				blink.serialWriteStub.serialPortStopReading(stopReadingRequest);
				// onNext will not be called after stopping, exit gracefully
			} else {
				SerialPort_Write_Request writeRequest = SerialPort_Write_Request.newBuilder().setDeviceName("BaseSerial").setData(value.getData()).build();
				blink.serialWriteStub.serialPortWrite(writeRequest);
			}
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
          Thread.sleep(1000);
      }

			
    } finally {
      blink.shutdown();
    }
  }
}

~~~~

---------------------------------

[Return to BLink services](blinkServices.md)
