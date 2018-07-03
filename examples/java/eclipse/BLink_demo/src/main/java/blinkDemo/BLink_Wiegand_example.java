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

  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_Wiegand_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing Wiegand service using the existing channel. */
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
			// Change config on c press
			System.out.println("New long : " + Long.toString(value.getData()));
			// Stop reading wiegand on this value 
			// In our case the value is sent from a home made wiegand device
//			if(value.getData() == 0x388f77d) {
//				Wiegand_StopReading_Request stopReadingRequest = Wiegand_StopReading_Request.newBuilder().setDeviceName("MezzWiegand")
//					.build();
//				blink.wiegandWriteStub.wiegandStopReading(stopReadingRequest);
//				// onNext will not be called after stopping, exit gracefully
//			}
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

