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
	        
	        // Now the same example using only set and get registers methods.
	        // One wire reset to get pulse
	        OneWire_SetRegister_Request setResetRequest = OneWire_SetRegister_Request.newBuilder().setDeviceName("MezzOneWire")
	        		.setReg(0xB4)
	        		.setData(0x00)
	        		.build();  // data is not used in this request
	        
	        blink.oneWireStub.oneWireSetRegister(setResetRequest);
	        
	        // Get status
	        // First set the read pointer register
	        OneWire_SetRegister_Request setStatusRequest = OneWire_SetRegister_Request.newBuilder().setDeviceName("MezzOneWire")
	        		.setReg(0xE1)
	        		.setData(0xF0)
	        		.build();
	        
	        blink.oneWireStub.oneWireSetRegister(setStatusRequest);
	        
	        // Now Read pointer
	        OneWire_GetRegister_Request getStatusRequest = OneWire_GetRegister_Request.newBuilder().setDeviceName("MezzOneWire")
	        		.setReg(0xE1)
	        		.build();
	        OneWire_GetRegister_Reply getStatusReply = blink.oneWireStub.oneWireGetRegister(getStatusRequest);
	        
	        // Print status
	        // Will give the whole value byte, see https://datasheets.maximintegrated.com/en/ds/DS2484.pdf
	        // to know the signification of each bits
	        System.out.println("Status register : " + getStatusReply.getValue());
	        
	    } finally {
	        blink.shutdown();
	    }
		
	}
}
