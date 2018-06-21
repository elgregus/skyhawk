package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_Network_example {
  
  private final ManagedChannel channel;
  private final BLink_Network NetworkService;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());
  
  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_Network_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing GPIO service using the existing channel. */
  public BLink_Network_example(ManagedChannel channel) {
    this.channel = channel;
    this.NetworkService = new BLink_Network(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_Network_example blink = new BLink_Network_example("localhost", 50051);
    try {
    
      int state = blink.NetworkService.GetState();
      
      logger.log(Level.INFO, "Network State: " + state);
      
    } finally {
      blink.shutdown();
    }
  }
}
