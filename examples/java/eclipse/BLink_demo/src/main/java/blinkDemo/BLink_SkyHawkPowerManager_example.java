package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_SkyHawkPowerManager_example {
  
  private final ManagedChannel channel;
//  private Network_ServiceBlockingStub networkStub;
  private BLink_SkyHawkPowerManager skyHawk;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());
  
  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_SkyHawkPowerManager_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing GPIO service using the existing channel. */
  public BLink_SkyHawkPowerManager_example(ManagedChannel channel) {
    this.channel = channel;
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_SkyHawkPowerManager_example blink = new BLink_SkyHawkPowerManager_example("localhost", 50051);
    try {
    	blink.skyHawk = new BLink_SkyHawkPowerManager(blink.channel);
    	
    	String fwVersion = blink.skyHawk.getFWVersion();
    	logger.log(Level.INFO, "skyHawkLPM: fwVersion is : " + fwVersion);
    	if(fwVersion.equals("0.00.001.01")) {
    		System.out.print(blink.skyHawk.updateFW("fwu_skyhawk_1.00.004.00.bin"));
    	} else if (fwVersion.equals("1.00.004.00")) {
    		System.out.print(blink.skyHawk.updateFW("fwu_skyhawk_0.00.001.01.bin"));
    	} else {
    		logger.log(Level.INFO, "skyHawkLPM: fwVersion Invalid : " + fwVersion);
    	}
    	
    	fwVersion = blink.skyHawk.getFWVersion();
    	
      logger.log(Level.INFO, "skyHawkLPM: fwVersion now is : " + fwVersion);
      
    } finally {
      blink.shutdown();
    }
  }
}
