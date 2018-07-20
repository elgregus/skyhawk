Accelerometer Service
============

The accelerometer service is used to use the accelerometer on the device.

---------------------------------

###Java Methods

####java.util.List[] getSamples():
The getSamples method is used to get available sample. <br>
This service is designed to buf data for 1 sec. After this delay, the first sample is removed to save the new one.<br>
Acceleration is given in g and the timestamp is given in ms since last boot.<br>
Note: startMonitoring() must be called before getSamples().

- param  : None

- return :
         + List[] data: { List<Double> x, List<Double> y, List<Double> z, List<Long> timestamp }

####void setDataRate(int dataRate):
The setDataRate method is used to set output date rate. <br>
This service is designed to use this value for the trigger and the streaming mode.<br>
Note: Output data rate in Hz. Higher value means higher CPU usage (caused by interrupts).

- param  :
         + int dataRate : Output data rate, can be 0, 10, 50, 100, 200, 400, 800.

- return : None

####void setRange(int axisRange):
The setRange method is used to set axis range. <br>
This service is designed to use this value for the trigger and the streaming mode.
- param  :
         + int axisRange : Range, can be 2, 4, 8.

- return : None

####void setTrigger(int threshold_x, int threshold_y, int threshold_z, int duration, int configuration):
The setTrigger method is used to set the interrupt generator.<br>
Note: Every axis has two registers (low and high for a total of 16 bits), but we only have<br>
one register for the threshold. So we must specify which one he is (low, high or both)
<br>

#####Configuration

Bit | Name | Description
--- | ---- | ------------
7   |  AOI | And/Or combination of Interrupt events.
6   |  6D  | 6-direction detection function enabled.
5   | ZHIE | Enable interrupt generation on Z high event.
4   | ZLIE | Enable interrupt generation on Z low event.
3   | YHIE | Enable interrupt generation on Y high event.
2   | YLIE | Enable interrupt generation on Y low event.
1   | XHIE | Enable interrupt generation on X high event.
0   | XLIE | Enable interrupt generation on X low event.

#####Threshold

Bit | Name | Description
--- | ---- | ------------
7:0 | THS  | Interrupt thresholds.

#####Duration

Bit | Name | Description
--- | ---- | ------------
7   | WAIT | Wait function enable.
6:0 | DUR  | Duration value.

<br>

- param  :
         + int threshold_x  : Axis thresholds, value between 0 and 255.
         + int threshold_y  : Axis thresholds, value between 0 and 255.
         + int threshold_z  : Axis thresholds, value between 0 and 255.
         + int duration     : Duration counter, value between 0 and 255.
         + int configuration: Interrupt generator configuration, value between 0 and 255.

- return : None

####void startMonitoring():
The startMonitoring method is used to start data acquisition.<br>
Use getSamples method to get accelerometer data.<br>
Note: StartMonitoring may take some time to start (non-blocking)

- param  : None

- return : None

####void startTrigger():
The startTrigger method is used to start interrupt generator (accel INT2 -> LPM).<br>
Use setTrigger method to set axis threshold and other configuration.

- param  : None

- return : None

####void stopMonitoring():
The stopMonitoring method is used to stop accelerometer data acquisition.

- param  : None

- return : None

####void stopTrigger():
The stopTrigger method is used to stop interrupt generator.

- param  : None

- return : None

---------------------------------

###Java Example
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
      // Set accelerometer (data rate: 50, range: +/- 2g)
      accelerometerService.setDataRate(50);
      accelerometerService.setRange(2);

      // Set trigger
      accelerometerService.setTrigger(150, 150, 150, 0, 42);

      // Start monitoring and trigger
      accelerometerService.startTrigger();
      accelerometerService.startMonitoring();

      // Get the last sample on X axis
      java.util.List[] samples = accelerometerService.getSamples();
      java.util.List<Double> x = samples[0];
      Double lastX = x.get(x.size() - 1);

      // Stop accelerometer
      accelerometerService.stopMonitoring();
      accelerometerService.stopTrigger();

    } finally {
      channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
~~~~

---------------------------------

[Return to BLink services](blinkServices.md)
