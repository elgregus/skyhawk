Accelerometer Service
============

The accelerometer service is used to use the accelerometer on the device.
Only the streaming mode has been implemented.

TODO:
- Implement trigger functionality
- Patch driver for timestamp channel

###Instantiation

~~~~{.java}
String host = "localhost";
int port = 50051;

final MannagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
final BLink_Accelerometer accelerometerService =  = new BLink_Accelerometer(channel);
~~~~

---------------------------------

###Methods

####java.util.List[] getSamples():
The getSamples method is used to get available sample. <br>
This service is designed to buf data for 1 sec. After this delay, the first sample is removed to save the new one.<br>
Note: start() must be called before getSamples().
- param  : None
- return :
         + List[] data: { List<Double> x, List<Double> y, List<Double> z }

####void start(int dataRate, int axisRange):
The start method is used to start data acquisition with a given data rate and a range (ex: +/- 4g).<br>
Note: Output data rate should be as low as possible. Higher value means higher CPU usage (caused by interrupts).
- param  : 
         + int dataRate : Output data rate, can be 0, 10, 50, 100, 200, 400, 800.
         + int axisRange : Range, can be 2, 4, 8.
- return : None
  
####void stop():
The stop method is used to stop accelerometer data acquisition.
- param  : None
- return : None

---------------------------------

###Example
This is a short example only covering the basic operations.<br>
For a more robust and detailed implementation, go see the example project file BLink_Accelerometer_example.java

~~~~{.java}
public class BLink_Accelerometer_example {

  String host = "localhost";
  int port = 50051;

  public void testAccelerometer() {
    final MannagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
    final BLink_Accelerometer accelerometerService =  = new BLink_Accelerometer(channel);
    
	try {
      // Start accelerometer (data rate: 400, range: +/- 2g)
      accelerometerService.start(400, 2);

      // Get samples and display the the last data on X axis
      java.util.List[] samples = blink.accelerometerService.getSamples();
      java.util.List<Double> x = samples[0];
      Double lastX = x.get(x.size() - 1);

      // Stop accelerometer
      blink.accelerometerService.stop();

    } finally {
      channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
~~~~

---------------------------------

[Return to BLink services](blinkServices.md)