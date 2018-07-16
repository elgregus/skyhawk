package blinkDemo;

import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.google.protobuf.ByteString;

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

  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_SerialPort_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing serialPort service using the existing channel. */
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
      SerialPort_Read_Request readRequest = SerialPort_Read_Request.newBuilder().setDeviceName("MezzSerial").build();
      
      blink.serialReadStub.serialPortRead(readRequest, new StreamObserver<blink_grpc.SerialPort_Read_Reply>() {
		public void onNext(SerialPort_Read_Reply value) {
			// Change config on c press
			System.out.println("New char : " + value.getData().toStringUtf8());
			if(value.getData().toStringUtf8().equals("c")) {
/*				SerialPort_Config_Request configRequest = SerialPort_Config_Request.newBuilder().setDeviceName("MezzSerial")
						.setBaudrate(9600)
						.setCharSize(8)
						.setFlowControl("none")
						.setParity("none")
						.setStopBits("one")
						.build();
				blink.serialWriteStub.serialPortConfig(configRequest);*/
			} else if(value.getData().toStringUtf8().equals("q")) {
				SerialPort_StopReading_Request stopReadingRequest = SerialPort_StopReading_Request.newBuilder().setDeviceName("MezzSerial")
					.build();
				blink.serialWriteStub.serialPortStopReading(stopReadingRequest);
				// onNext will not be called after stopping, exit gracefully
			} else {
//				SerialPort_Write_Request writeRequest = SerialPort_Write_Request.newBuilder().setDeviceName("MezzSerial").setData(value.getData()).setMode("RS485").build();
//				blink.serialWriteStub.serialPortWrite(writeRequest);
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
    	  SerialPort_Write_Request writeRequest = SerialPort_Write_Request.newBuilder().setDeviceName("MezzSerial").setData(ByteString.copyFromUtf8("Testing one two")).setMode("RS485").build();
	      blink.serialWriteStub.serialPortWrite(writeRequest);
      }

			
    } finally {
      blink.shutdown();
    }
  }
}
