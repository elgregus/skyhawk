OneWire Service
============

The OneWire Service is used to communicate with the OneWire port on the device. You may write, read or triplet on this port. No configuration is required, but you can still set a few options if you like.

Important note : The OneWire port is plexed with the Wiegand port. You need to [use the SkyHawkMisc Service](skyHawkMiscService.md) to select which mode you want to use.

The methods described and the example below will make direct use of gRPC calls.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Skyhawk Capabilities
Here is a list of capabilites for each configuration registers

#### DEVICE CONFIGURATION
Field          | bit     | description                                  
---------------|-------- | --------------------------------------------
Active Pullup  | 0       | true : Activates the active pullup. (low impedence transistor) <br>false : Activates the passive pullup. (RWPU resistor)
Power Down     | 1       | true : Powers down the OneWire line. <br>false : Powers up the OneWire line.                                   
Strong Pullup  | 3       | true : Activates the strong pullup. <br>false : Deactivates the strong pullup.
OneWire Speed  | 4       | true : Activates the overdrive speed. <br>false : Deactivates the overdrive speed.
See Maxim's documentation for use cases : https://datasheets.maximintegrated.com/en/ds/DS2484.pdf

##### Default Configurations
**Active PullUp :** true<br>
**Power Down :** false<br>
**Strong PullUp :** false<br>
**OneWire Speed :** false<br>


#### ADJUST ONEWIRE PORT
Field          | bit     | description                                  
---------------|-------- | --------------------------------------------
P2 -> P0       | 7:5     | Select which parameter you want to change it's value. <br> 000: selects tRSTL <br>001: selects tMSP <br>010: selects tW0L <br>011: selects tREC0; the OD flag does not apply (donâ€™t care) <br>100: selects RWPU; the OD flag does not apply (donâ€™t care)
OD             | 4       | true : Value applies to overdrive speed. <br>false : Value applies to standard speed.                                  
Value          | 3:0     | Parameter value code, see conversion table below.
See Maxim's documentation for more information : https://datasheets.maximintegrated.com/en/ds/DS2484.pdf

##### CONVERSION TABLE
| Parameter Value Code | 000 : tRSTL (us) || 001 : tMSP (us)  || 010 : tW0L (us) || 011 : tREC0 (us) | 100 : RWPU (ohm) |
|:--------------------:|:-------:|:------:|:-------:|:-------:|:-------:|:-------:|:----------------:|:----------------:|
|                      | OD = 0  | OD = 1 | OD = 0  | OD = 1  | OD = 0  | OD = 1  | OD = N/A         | OD = N/A         |
| 0000                 | 440     | 44     | 58      | 5.5     | 52      | 5.0     | 2.75             | 500              |
| 0001                 | 460     | 46     | 58      | 5.5     | 54      | 5.5     | 2.75             | 500              |
| 0010                 | 480     | 48     | 60      | 6.0     | 56      | 6.0     | 2.75             | 500              |
| 0011                 | 500     | 50     | 62      | 6.5     | 58      | 6.5     | 2.75             | 500              |
| 0100                 | 520     | 52     | 64      | 7.0     | 60      | 7.0     | 2.75             | 500              |
| 0101                 | 540     | 54     | 66      | 7.5     | 62      | 7.5     | 2.75             | 500              |
| **0110**             | **560** | **56** | **68**  | **8.0** | **64**  | **8.0** | **5.25**         | **1000**         |
| 0111                 | 580     | 58     | 70      | 8.5     | 66      | 8.5     | 7.75             | 1000             |
| 1000                 | 600     | 60     | 72      | 9.0     | 68      | 9.0     | 10.25            | 1000             |
| 1001                 | 620     | 62     | 74      | 9.5     | 70      | 9.5     | 12.75            | 1000             |
| 1010                 | 640     | 64     | 76      | 10.0    | 70      | 10.0    | 15.25            | 1000             |
| 1011                 | 660     | 66     | 76      | 10.5    | 70      | 10.0    | 17.75            | 1000             |
| 1100                 | 680     | 68     | 76      | 11.0    | 70      | 10.0    | 20.25            | 1000             |
| 1101                 | 700     | 70     | 76      | 11.0    | 70      | 10.0    | 22.75            | 1000             |
| 1110                 | 720     | 72     | 76      | 11.0    | 70      | 10.0    | 25.25            | 1000             |
| 1111                 | 740     | 74     | 76      | 11.0    | 70      | 10.0    | 25.25            | 1000             |
**Note**: The power-on default values are **bold**.

##### Default Configurations
**Value code :** 0110 (see table above)

---------------------------------

### Java Methods

Since gRPC always uses a system of request and reply, to simplify this documentation paramters in the request object have simply been tagged as param and return values in the reply object have been tagged as return values.

#### void OneWire_Read(String deviceName, int numberOfBytes):

The Read method is used to read on the OneWire port of the device.
It will return an array containing the bytes that were read starting with the **most recent byte.**

- param  :
         + String deviceName : Name of the device.
         + int numberOfBytes : Number of bytes you desire to read. (min 1)
- return : Array containing the bytes that were read starting with the **most recent byte.**


#### void OneWire_Write(String deviceName, String mode):

The Write method is used to write on the OneWire port of the device.
The mode parameter switches between the write bytes or the write bit fonctionnality.

Accepted values are : <br>
"oneBit" : Writes a byte.<br>
"byte" : Writes a bit. Since the bit is still sent as an integer, put the requested value as the **LSB**

- param  :
         + String deviceName : Name of the device.
         + String mode : Type of write you desire ; "oneBit" or "byte"
- return : None


#### void OneWire_Triplet(String deviceName, bool direction):

Does a OneWire triplet, see Maxim's documentation and application notes for more detail : <br>
https://datasheets.maximintegrated.com/en/ds/DS2484.pdf <br>
https://www.maximintegrated.com/en/app-notes/index.mvp/id/3684

This method returns the state of the OneWire line.

- param  :
         + String deviceName : Name of the device.
         + bool direction : Direction of the Triplet 
- return : 
         + bool power : Is the device powered on.
         + bool overdrive : Is the speed in overdrive.
         + bool strongPullUp : Is the strong pullup activated.
         + bool activePullUp : Is the active pullup activated.
         + bool pulseDetected : Is the pulse detected.
         + bool logicLevel : Current logic level.
         + bool oneWireBusy : Is the line busy.
         + bool shortDetected : Was a short detected.
         + bool deviceReset : Was the device reset.
         + bool singleBitResult :Was the result a single bit.
         + bool tripletSecondBit : State of the triplet second bit.
         + bool branchDir : Current branch direction.
         
         
#### void OneWire_PowerOff(String deviceName):

Power off the OneWire line.

- param  :
         + String deviceName : Name of the device.
- return : None


#### void OneWire_PowerOn(String deviceName):

Power on the OneWire line.

- param  : 
         + String deviceName : Name of the device.
- return : None


#### void OneWire_AdjustPort(String deviceName, uint8_t data):

The AdjustPort method is used to Adjust the port of the OneWire device.
Refer to the section ADJUST ONEWIRE PORT in Skyhawk Capabilities above to use this method.

- param  :
         + String deviceName : Name of the device.
         + uint8_t data : Data coresponding to a setting in the adjust onewire port parameter.
- return :
         + uint8_t value : Current value of the sent parameter. (to validate settings)
         

#### void OneWire_GetStatus(String deviceName):

This method returns the state of the OneWire line.

- param  :
         + String deviceName : Name of the device.
- return : 
         + bool power : Is the device powered on.
         + bool overdrive : Is the speed in overdrive.
         + bool strongPullUp : Is the strong pullup activated.
         + bool activePullUp : Is the active pullup activated.
         + bool pulseDetected : Is the pulse detected.
         + bool logicLevel : Current logic level.
         + bool oneWireBusy : Is the line busy.
         + bool shortDetected : Was a short detected.
         + bool deviceReset : Was the device reset.
         + bool singleBitResult :Was the result a single bit.
         + bool tripletSecondBit : State of the triplet second bit.
         + bool branchDir : Current branch direction.
         
         
#### void OneWire_SetOverdriveSpeed(String deviceName, bool overdrive):

Sets the overdrive speed.

- param  : 
         + String deviceName : Name of the device.
         + bool overdrive : true : activate overdrive, false : deactivate overdrive.
- return : None


#### void OneWire_Reset(String deviceName):

Reset the OneWire line.

- param  :
         + String deviceName : Name of the device.
- return : None


#### void OneWire_SetRegister(String deviceName, uint8_t reg, uint8_t data):

Set a specific register of the OneWire controller, see https://datasheets.maximintegrated.com/en/ds/DS2484.pdf for a list of all registers and how to use them.

- param  :
         + String deviceName : Name of the device.
         + uint8_t reg : Register you want to set.
         + uint8_t data : Data you want to set the register to.
- return : None


#### void OneWire_GetRegister(String deviceName, uint8_t reg):

Get a specific register of the OneWire controller, see https://datasheets.maximintegrated.com/en/ds/DS2484.pdf for a list of all registers and how to use them.

- param  :
         + String deviceName : Name of the device.
         + uint8_t reg : Register you want to get.
- return :
         + uint8_t value : Value of the requested register

---------------------------------

### Java Example

This is a short example only covering the basic operations.<br>
To run this example, plug a OneWire device in the OneWire port. This example will simply power on the device, and show the current status of the device.

~~~~{.java}
package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;

import blink_grpc.BLink;
import blink_grpc.OneWire_Read_Reply;
import blink_grpc.OneWire_Read_Request;
import blink_grpc.OneWire_Write_Reply;
import blink_grpc.OneWire_Write_Request;
import blink_grpc.OneWire_Triplet_Reply;
import blink_grpc.OneWire_Triplet_Request;
import blink_grpc.OneWire_GetStatus_Reply;
import blink_grpc.OneWire_GetStatus_Request;
import blink_grpc.OneWire_PowerOn_Reply;
import blink_grpc.OneWire_PowerOn_Request;
import blink_grpc.OneWire_PowerOff_Reply;
import blink_grpc.OneWire_PowerOff_Request;
import blink_grpc.OneWire_SetOverdriveSpeed_Reply;
import blink_grpc.OneWire_SetOverdriveSpeed_Request;
import blink_grpc.OneWire_Reset_Reply;
import blink_grpc.OneWire_Reset_Request;
import blink_grpc.OneWire_SetRegister_Reply;
import blink_grpc.OneWire_SetRegister_Request;
import blink_grpc.OneWire_GetRegister_Reply;
import blink_grpc.OneWire_GetRegister_Request;
import blink_grpc.OneWire_ServiceGrpc;
import blink_grpc.OneWire_ServiceGrpc.OneWire_ServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_OneWire_example {

	  private final ManagedChannel channel;
	  private OneWire_ServiceBlockingStub oneWireStub;
	  
	  private static final Logger logger = Logger.getLogger(BLink_OneWire_example.class.getName());

	/** Construct client connecting to BLink server at {@code host:port}. */
	public BLink_OneWire_example(String host, int port) {
	    this(ManagedChannelBuilder.forAddress(host, port)
	        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
	        // needing certificates.
	        .usePlaintext(true).build());
	}
	
	/** Construct client for accessing serialPort service using the existing channel. */
	public BLink_OneWire_example(ManagedChannel channel) {
	    this.channel = channel;
	}

	public void shutdown() throws InterruptedException {
	    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	  }

	public static void main(String[] args) throws Exception {
		final BLink_OneWire_example blink = new BLink_OneWire_example("localhost", 50051);
	    try {
	    	blink.oneWireStub = OneWire_ServiceGrpc.newBlockingStub(blink.channel);
	    	
	        // One wire reset to get pulse
	        OneWire_Reset_Request resetRequest = OneWire_Reset_Request.newBuilder().setDeviceName("MezzOneWire").build();
	        blink.oneWireStub.oneWireReset(resetRequest);
	        
	        // Get status
	        OneWire_GetStatus_Request statusRequest = OneWire_GetStatus_Request.newBuilder().setDeviceName("MezzOneWire").build();
	        OneWire_GetStatus_Reply statusReply = blink.oneWireStub.oneWireGetStatus(statusRequest);
	    	
	        // Print status
	        System.out.println("Power : " + statusReply.getPower());
	        System.out.println("ODSpeed : " + statusReply.getOverdrive());
	        System.out.println("Strong PullUp : " + statusReply.getStrongPullUp());
	        System.out.println("Active PullUp : " + statusReply.getActivePullUp());
	        System.out.println("Pulse detected : " +statusReply.getPulseDetected());
	        System.out.println("Logic Level : " +statusReply.getLogicLevel());
	        System.out.println("1Wire Busy :" + statusReply.getOneWireBusy());
	        System.out.println("Short Detected : " + statusReply.getShortDetected());
	        System.out.println("Device Reset : " + statusReply.getDeviceReset());
	        System.out.println("Single Bit Result : " + statusReply.getSingleBitResult());
	        System.out.println("Triplet Second bit : " + statusReply.getTripletSecondBit());
	        System.out.println("Branch Direction : " + statusReply.getBranchDir());
	        
	    } finally {
	        blink.shutdown();
	    }
		
	}
}

~~~~

---------------------------------

[Return to BLink services](blinkServices.md)
