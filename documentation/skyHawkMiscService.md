Skyhawk Miscellaneous Service
============

The Skyhawk Miscellaneous Service is used to control various small components on the skyhawk board.

The methods described and the example below will make direct use of gRPC calls.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Java Methods

Since gRPC always uses a system of request and reply, to simplify this documentation paramters in the request object have simply been tagged as param and return values in the reply object have been tagged as return values.

#### void WiegandEN_OneWireDIS_Set(boolean requestedState):

Toogle between Wiegand and OneWire.

True      | false
--------- | ---------
Wiegand   | OneWire

- param  :
         + boolean resquestedState : State you want the Wiegand/OneWire port to be in.
- return : None
		 
#### boolean WiegandEN_OneWireDIS_Get()

Get the current state of the Wiegand/OneWire port.

True      | false
--------- | ---------
Wiegand   | OneWire

- param  : None
- return : 
         + boolean : Current state of the Wiegand/OneWire port.

#### void WWANAntennaEN_Set(boolean requestedState):

Toogle the WWANAntenna between internal and external antenna.

True      | false
--------- | ---------
external  | internal

- param  :
         + boolean resquestedState : State you want the WWANAntenna to be.
- return : None
		 
#### boolean WWANAntennaEN_Get()

Get the current state of the WWANAntenna.

True      | false
--------- | ---------
external  | internal

- param  : None
- return : 
         + boolean : Current state of the WWANAntenna.

#### void PoEEN_Set(boolean requestedState):

Toogle the PoE on/off.

True      | false
--------- | ---------
On        | Off

- param  :
         + boolean resquestedState : State you want the PoE to be.
- return : None
		 
#### boolean PoEEN_Get()

Get the current state of the PoE.

True      | false
--------- | ---------
On        | Off

- param  : None
- return : 
         + boolean : Current state of the PoE.

#### void MezzSerialMode_Set(string mode):

Set the mode of the Mezz serial port.

Name      | Value     | Description
--------- | --------- | -------------------------------------------
RS232     | "RS232"   | Set the Mezz serial port in RS232 mode
RS485     | "RS485"   | Set the Mezz serial port in RS485 mode
shutdown  | "SHDN"    | Shutdown the serial port of the Mezz

- param  :
         + string mode : Mode you want the Mezz serial port to be.
- return : None
		 
#### string MezzSerialMode_Get()

Get the mode of the Mezz serial port.

Name      | Value     | Description
--------- | --------- | -------------------------------------------
RS232     | "RS232"   | Set the Mezz serial port in RS232 mode
RS485     | "RS485"   | Set the Mezz serial port in RS485 mode
shutdown  | "SHDN"    | Shutdown the serial port of the Mezz

True      | false
--------- | ---------
On        | Off

- param  : None
- return : 
         + string : Current mode of the Mezz serial port.
         
#### boolean DetectMezz()

Detect if a Mezz is currently plugged-in. (Active low)

True             | false
---------------- | ---------
No Mezz on board | Mezz is connected to base board

- param  : None
- return : 
         + boolean : Is there a Mezz board connected to the base board (Active low).
         
---------------------------------

### Java Example

This is a short example only covering the basic operations.<br>

~~~~{.java}
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
import blink_grpc.SkyhawkMisc_ServiceGrpc;
import blink_grpc.SkyhawkMisc_ServiceGrpc.SkyhawkMisc_ServiceBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_SkyHawkMisc_example {

  private final ManagedChannel channel; 
  private SkyhawkMisc_ServiceBlockingStub skyhawkMiscStub;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());

  // Construct client connecting to BLink server.
  public BLink_SkyHawkMisc_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  // Construct client for accessing GPIO service using the existing channel.
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
      blink.skyhawkMiscStub = SkyhawkMisc_ServiceGrpc.newBlockingStub(blink.channel);    

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

~~~~

---------------------------------

[Return to BLink services](blinkServices.md)
