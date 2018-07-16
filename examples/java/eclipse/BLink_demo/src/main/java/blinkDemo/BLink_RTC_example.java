package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_RTC_example {
  
  private final ManagedChannel channel;
  private final BLink_RTC RTCService;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());
  
  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_RTC_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing GPIO service using the existing channel. */
  public BLink_RTC_example(ManagedChannel channel) {
    this.channel = channel;
    this.RTCService = new BLink_RTC(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_RTC_example blink = new BLink_RTC_example("localhost", 50051);
    try {
    	
    	blink.RTCService.SetTime(1528478686);  // 6 June 2018
    
      long time = blink.RTCService.GetTime();
      
      logger.log(Level.INFO, "Time is : " + time);
      
    } finally {
      blink.shutdown();
    }
  }
}
