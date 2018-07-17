package blinkDemo;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.Timer;
import java.util.TimerTask;

public class BLink_Accelerometer_example {

  private final ManagedChannel channel;
  private final BLink_Accelerometer accelerometerService;

  private static final Logger logger = Logger.getLogger(BLink_SkyHawkPowerManager_example.class.getName());

  /** Construct client connecting to BLink server at {@code host:port}. */
  public BLink_Accelerometer_example(String host, int port) {
    this(ManagedChannelBuilder.forAddress(host, port)
        // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
        // needing certificates.
        .usePlaintext(true).build());
  }

  /** Construct client for accessing Accelerometer service using the existing channel. */
  public BLink_Accelerometer_example(ManagedChannel channel) {
    this.channel = channel;
    this.accelerometerService = new BLink_Accelerometer(channel);
  }

  public void shutdown() throws InterruptedException {
    channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
  }

  public static void main(String[] args) throws Exception {
    final BLink_Accelerometer_example blink = new BLink_Accelerometer_example("localhost", 50051);
    try {

      // Accelerometer start example
      // Valid output data rate: 0, 10, 50, 100, 200, 400, 800
      // Valid range: 2, 4, 8
      blink.accelerometerService.start(400, 2);

      // Fetch data every second to display the latest data and the buffers count on each axis
      TimerTask displayLastData = new TimerTask() {
          @Override
          public void run() {
              java.util.List[] samples = blink.accelerometerService.getSamples();
              java.util.List<Double> x = samples[0];
              java.util.List<Double> y = samples[1];
              java.util.List<Double> z = samples[2];
              java.util.List<Long> timestamp = samples[3];

              Double lastX = x.get(x.size() - 1);
              Double lastY = y.get(y.size() - 1);
              Double lastZ = z.get(z.size() - 1);
              Long lastTimestamp = timestamp.get(timestamp.size() - 1);

              logger.log(Level.INFO, "Count  [x y z]: " + x.size() + " " + y.size() + " " + z.size());
              logger.log(Level.INFO, "Last   [x y z]: " + lastX + " " + lastY + " " + lastZ);
              logger.log(Level.INFO, "Last Timestamp: " + lastTimestamp);
          }
      };
      Timer timer = new Timer();
      timer.schedule(displayLastData, 0, 1000);

      // Wait 1 min. and stop this example
      TimeUnit.SECONDS.sleep(60);
      timer.cancel();
      timer.purge();
      blink.accelerometerService.stop();

    } finally {
      blink.shutdown();
    }
  }
}
