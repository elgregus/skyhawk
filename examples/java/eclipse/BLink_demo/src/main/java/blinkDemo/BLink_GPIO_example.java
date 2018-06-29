package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_GPIO_example {

  private final ManagedChannel channel;
  private final BLink_GPIO gpioService;  

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());

  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_GPIO_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing GPIO service using the existing channel. */
  public BLink_GPIO_example(ManagedChannel channel) {
    this.channel = channel;
    this.gpioService = new BLink_GPIO(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_GPIO_example blink = new BLink_GPIO_example("localhost", 50051);
    try {
     
      // GPIO Set Example
      blink.gpioService.setGPIO("DOUT3", true);
      blink.gpioService.setGPIO("DOUT1", false);

      // GPIO Get Example
      Boolean value = blink.gpioService.getGPIO("DIN1");

      logger.log(Level.INFO, "GPIO value: " + value);
			
    } finally {
      blink.shutdown();
    }
  }
}
