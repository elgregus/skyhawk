SkyHawkPowerManager Service
============

The SkyHawkPowerManager service is used to get and set registers in the LPM, it is also used to send updates to the module.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

### Instantiation

~~~~{.java}
String host = "localhost";
int port = 50051;

final MannagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
final BLink_SkyHawkPowerManager skyHawkPowerManagerService =  = new BLink_SkyHawkPowerManager(channel);
~~~~

---------------------------------

### Methods

Do not forget that every methods always returns the error structure in addition to any other return value.

#### void setRegister(String regName, int value):

The setRegister method is used to set a specific Register in the LPM.<br>
To see a complete list of all available registers and accepted values, see section constants below.

- param  : 
         + String name   : Name of the Register we want to switch the state.
         + int value     : Value you want to set the register to.
- return : None
  
#### int getRegister(String regName):

The getRegister method is used to get a specific Register in the LPM.<br>
To see a complete list of all available registers and accepted values, see section constants below.

- param  : 
         + String name   : Name of the Register we want to get the state.		  
- return : 
         + int : Value of the register.

#### bool updateFW(String filePath):

The updateFw method is used to update the firmware in the LPM.<br>
This call is quite long (can take up to 10 minutes) so it should be called in its own thread.

- param  : 
         + String filePath   : Path of the new .bin file to update the firmware with.
- return : 
         + Boolean : true for success, false for failure.

#### String getFWVersion():

The getFWVersion method is used to get the actual firmware version in the LPM.

- param  : None
- return : 
         + String : The string contains the firmware version, is maximum 30 characters long and is formatted as follow "0.00.000.00"

#### int getHWVersion():

The getHWVersion method is used to get the actual hardware version of the board.

- param  : None
- return : 
         + int : The hardware version number

---------------------------------

### Constants

#### Registers:

Here is the list of all Registers you can pass as parameters for the get and set methods.

Name                 | Possible values | Default Value | Access | Description
-------------------- | --------------- | ------------- | ------ | -----------
CPU_WAKE_VIN         | 0 or 1          | 1             | RO     | Configure VIN condition to enable CPU and move from low<brpower to normal mode.
CPU_WAKE_IGN         | 0 or 1          | 0             | RW     | Configure ignition event to enable CPU and move from low<br>power to normal mode.
CPU_SHUTDOWN_LPM_FWU | 0 or 1          | 1             | RO     | Configure LPM reboot request from firmware update to disable<br>CPU and move from normal to low power mode.
CPU_SHUTDOWN_TOPL    | 0 or 1          | 0             | RW     | Configure timeout power loss to disable CPU and move from<br>normal to low power mode.
CPU_SHUTDOWN_TOLP    | 0 or 1          | 0             | RW     | Configure timeout low power to disable CPU and move from<br>normal to low power mode.
CPU_WAKE_DELAY       | 0 to 65536 (ms) | 0             | RW     | Configure the delay before enabling CPU following wake event.
POWER_CTRL_VREFEN    | 0 or 1            | 0             | RW     | Enable VREF.
POWER_CTRL_VRESEL    | 0 : 5V<br>1 : VIN | 0             | RW     | Select VREF
POWER_CTRL_VRELPEN   | 0 or 1            | 0             | RW     | Enable VREF in low power mode.
POWER_CTRL_GPSEN     | 0 or 1            | 0             | RW     | Enable GPS.
POWER_CTRL_GPSLPEN   | 0 or 1            | 0             | RW     | Enable GPS in low power mode.
POWER_CTRL_BPEN      | 0 or 1            | 0             | RO     | Enable bitPipe
POWER_CTRL_BPLPEN    | 0 or 1            | 0             | RO     | Enable bitPipe in low power mode.
VIN_RTC_MODE         | 0 to 65536(mV)    | 0             | RW     | Configure the input voltage (VIN) threshold for VBAT Shelf<br>Mode.<br>If VIN falls below THRESHOLD - HYSTERESIS, LPM transitions<br>to Shelf Mode.<br>If VIN rises above THRESHOLD + HYSTERESIS, LPM transitions<br>to RTC Only.
VIN_LOW_POWER_MODE   | 0 to 65536(mV)    | 0             | RW     | Configure the input voltage (VIN) threshold for VIN Low<br>Power Mode.<br>If VIN falls below THRESHOLD - HYSTERESIS, LPM transitions<br>to Low Power Mode.<br>If VIN rises above THRESHOLD + HYSTERESIS, LPM transitions<br>to Normal (Low Voltage) Mode.
VIN_NORM_LV_MODE     | 0 to 65536(mV)    | 0             | RW     | Configure the input voltage (VIN) threshold for VIN Normal<br>(Low Voltage) Mode.<br>If VIN falls below THRESHOLD - HYSTERESIS, LPM transitions<br>to Normal (Low Voltage) Mode.<br>If VIN rises above THRESHOLD + HYSTERESIS, LPM transitions<br>to Normal Mode.
VIN_HYSTERESIS       | 0 to 65536(mV)    | 200           | RW     | Configure the VIN hysteresis before to change the power mode.
IRQ_MODECHGEN        | 0 or 1            | 1             | RW     | Enable mode change IRQ.
IRQ_VBATTLOWEN       | 0 or 1            | 1             | RW     | Enable VBATT low alarm IRQ.
IRQ_VINLOWEN         | 0 or 1            | 1             | RW     | Enable VIN low alarm IRQ.
IRQ_CPUSHDN          | 0 or 1            | 1             | RO     | Enable LPM request a CPU shutdown IRQ.
POWER_LOSS_TIMEOUT   | 0 : Disable<br>1 to 65535 : Timeout(s)  | 0     | RW | Configure the timeout after what the CPU will be powered<br>off even if it's not correctly shutdown.<br>Time timeout will start after the VIN RTC Mode threshold<br>is met.
LOW_POWER_TIMEOUT    | 0 : Disable<br>1 to 65535 : Timeout(s)  | 0     | RW | Configure the timeout after what the CPU will be powered off<br>even if it's not correctly shutdown.<br>Time timeout will start after the VIN Low Power Mode<br>threshold is met.
CPU_SHUTDOWN_TIMEOUT | 0 : Disable<br>1 to 65535 : Timeout(s)  | 0     | RW | Configure the timeout after what the CPU will be powered off<br>even if it's not correctly shutdown.<br>Time timeout will start after a shutdown requested by<br>CPU or when a fw update reboot command is requested.
LAST_WAKE_VIN        | 0 or 1          | 0             | RO | Last wake event that powered the CPU is VIN condition.
LAST_WAKE_IGN        | 0 or 1          | 0             | RO | Last wake event that powered the CPU is ignition event.
VIN_STATUS           | 0 to 65536(mV)  | 0             | RO | Read the current VIN voltage.<br>In RTC Mode and Low Power Mode, VIN will be sampled at 1 Hz.<br>In Normal Mode, VIN will be sampled at 10 Hz.
POWER_STATUS_PM      | 000: Shutdown / Shelf Mode (not used)<br>001: RTC Only (Not used)<br>010: Low power mode<br>011: Normal (Low voltage)<br>100: Normal | 0 | RO | Read the current power mode.
POWER_STATUS_CHGSTS  | 00: Charge complete<br>01: Charging<br>10: Safety timer fault<br>11: Cell temperature invalid<br> | 0 | RO | Read the current charge status.
IRQ_STATUS_MODECHG   | 0 or 1          | 0             | RO | Read power mode change IRQ status.
IRQ_STATUS_VINLOW    | 0 or 1          | 0             | RO | Read VIN under low power threshold IRQ status.
IRQ_STATUS_CPUSHDN    | 0 or 1          | 0             | RO | LPM request a CPU shutdown




[comment]: <> ( CPU_WAKE_RTC         | 0 or 1          | 0             | RW     | Configure RTC timeout to enable CPU and move from low power<br>to normal mode.)
[comment]: <> (CPU_WAKE_DI1         | 0 or 1          | 0             | RW     | Configure digital input event 1 to enable CPU and move from<br>low power to normal mode. CPU_WAKE_DI2         | 0 or 1          | 0             | RW     | Configure digital input event 2 enable CPU and move from low<br>power to normal mode.)
[comment]: <> (CPU_WAKE_ACC         | 0 or 1          | 0             | RW     | Configure Accelerometer event to enable CPU and move from<br>low power to normal mode.)
[comment]: <> (CPU_WAKE_CAN         | 0 or 1          | 0             | RW     | Configure CAN activity event to enable CPU and move from low<br>power to normal mode.)
[comment]: <> (CPU_SHUTDOWN_LPM_REQ | 0 or 1          | 1             | RW     | Configure CPU shutdown request to disable CPU and move from<br>normal to low power mode.)


[comment]: <> (DIG1_POLARITY        | 00 : Disable <br>01 : Rising edge <br>10 : Falling edge <br>11 : both edge | 00 | RW | Configure digital input polarity 1.)
[comment]: <> (DIG2_POLARITY        | 00 : Disable <br>01 : Rising edge <br>10 : Falling edge <br>11 : both edge | 00 | RW | Configure digital input polarity 2.)
[comment]: <> (POWER_CTRL_RLP       | 0 or 1            | 0             | RW     | Request Low Power mode.)
[comment]: <> (POWER_CTRL_RSM       | 0 or 1            | 0             | RW     | Request shelf mode.)
[comment]: <> (GPS_EXT_ANT_CTRL     | 0 or 1            | 1             | RW     | Enable the external entennas of the GPS.)
[comment]: <> (VBAT_SHELF_MODE      | 0 to 65536(mV)    | 0             | RW     | Configure the battery voltage (VBAT) threshold for VBAT<br>Shelf Mode.<br>If VBAT falls below THRESHOLD - HYSTERESIS, LPM transitions<br>to Shelf Mode.<br>If VBAT rises above THRESHOLD + HYSTERESIS, LPM transitions<br>to RTC Only.)
[comment]: <> (VBAT_HYSTERESIS      | 0 to 65536(mV)    | 100           | RW     | Configure the VBAT hysteresis before to change the power mode.)
[comment]: <> (IRQ_KAWARN           | 0 or 1            | 1             | RO     | Enable keep alive warning IRQ.)
[comment]: <> (LP_MODE_REQ_DELAY    | 0 : Disable<br>1 to 65535 : Delay(ms)   | 15000 | RW | Configure the delay after what the CPU will be powered off<br>even if it's not correctly shutdown.<br>The delay will start after the CPU launch a Low Power<br>Mode Request.)
[comment]: <> (CPU_COLD_KA_TIMEOUT  | 0 : Disable<br>1 to 65535 : Timeout(ms) | 0     | RW | Configure the grace period before requiring the first kick<br>from the CPU.)
[comment]: <> (CPU_KA_TIMEOUT       | 0 : Disable<br>1 to 65535 : Timeout(ms) | 0     | RW | Configure the time after which the LPM will need to be<br>"kicked" by the CPU to keep the power on.)
[comment]: <> (VBAT_STATUS          | 0 to 65536(mV)  | 0             | RO | Read the current VBAT voltage.<br>In RTC Mode and Low Power Mode, VBAT will be sampled at 1 Hz.<br>In Normal Mode, VBAT will be sampled at 10 Hz.)
[comment]: <> (GPS_ANT_DET          | 0 or 1          | 0             | RO | Read GPS external antenna detected.)
[comment]: <> (GPS_ANT_SHRT         | 0 or 1          | 0             | RO | Read GPS external antenna short.)
[comment]: <> (POWER_STATUS_VBATT   | 0 to 100 (%)    | 0             | RO | Read the current Battery voltage in %.)
[comment]: <> (IRQ_STATUS_VBATTLOW  | 0 or 1          | 0             | RO | Read VBATT low IRQ status.)
[comment]: <> (IRQ_STATUS_KAWARN    | 0 or 1          | 0             | RO | Read keep alive warning IRQ status.<br>Keep Alive Warning occurs when CPU forgot to kick the LPM<br>for a keep alive timeout cycle.<br>The LPM will trigger the CPU reset after the second keep<br>alive timeout cycle.)
[comment]: <> (LAST_WAKE_RTC        | 0 or 1          | 0             | RO | Last wake event that powered the CPU is RTC timeout.)
[comment]: <> (LAST_WAKE_DI1        | 0 or 1          | 0             | RO | Last wake event that powered the CPU is digital input 1 event.)
[comment]: <> (LAST_WAKE_DI2        | 0 or 1          | 0             | RO | Last wake event that powered the CPU is digital input 2 event.)
[comment]: <> (LAST_WAKE_ACC        | 0 or 1          | 0             | RO | Last wake event that powered the CPU is Accelerometer event.)
[comment]: <> (LAST_WAKE_CAN        | 0 or 1          | 0             | RO | Last wake event that powered the CPU is CAN activity event.)



[comment]: <> (===========================================)
[comment]: <> (To Discuss:  Do we really wanna give them access to theses registers?)
[comment]: <> (===========================================)

[comment]: <> (FW_UPDT_STS_ERASE    | 00 : Idle<br>01 : Command in progress<br>10 : Command failed<br>11 : Command succed | 0 | RO | Read erase command status (cleared on read).<br>Fail reason: Cannot erase flash.)
[comment]: <> (FW_UPDT_STS_WRITE    | 00 : Idle<br>01 : Command in progress<br>10 : Command failed<br>11 : Command succed | 0 | RO | Read write command status (cleared on read).<br>Fail reason: Cannot write data from "Write Buffer" to flash.)
[comment]: <> (FW_UPDT_STS_WRITECRC | 00 : Idle<br>01 : Command in progress<br>10 : Command failed<br>11 : Command succed | 0 | RO | Read writeCRC command status (cleared on read).<br>Fail reason: "Write buffer" CRC doesn't match with the<br>WRITECRC command payload.)
[comment]: <> (FW_UPDT_STS_WRITEADR | 00 : Idle<br>01 : Command in progress<br>10 : Command failed<br>11 : Command succed | 0 | RO | Read writeADR command status (cleared on read).<br>Fail reason: The address from WRITEADR payload is out of range.)
[comment]: <> (FW_UPDT_STS_VALIDATE | 00 : Idle<br>01 : Command in progress<br>10 : Command failed<br>11 : Command succed | 0 | RO | Read validate command status (cleared on read).<br>Fail reason: Application CRC doesn't match with the VALIDATE<br>command payload.)
[comment]: <> (FW_UPDT_STS_REBOOT   | 00 : Idle<br>01 : Command in progress<br>10 : Command failed<br>11 : Command succed | 0 | RO | Read reboot command status (cleared on read).<br>)

### Example
This is a short example only covering the basic operations.<br>
For a more robust and detailed implementation, go see the example project file BLink_SkyHawkPowerManager_example.java
~~~~{.java}
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_SkyHawk_example {

  String host = "localhost";
  int port = 50051;

  public void testSkyHawkLPM() {
    final MannagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
    final BLink_SkyHawkPowerManager SkyHawkPowerManagerService = new BLink_SkyHawkPowerManager(channel);
    
	try {
      // Get the FWVersion
      String fwVersion = SkyHawkPowerManager.getFWVersion();
	  
	  SkyHawkPowerManager.setRegister(CPU_WAKE_DELAY, 1000);
	  System.out.print(SkyHawkPowerManager.getRegister(CPU_WAKE_DELAY));
	  
	  // Update
	  logger.log(Level.INFO, "skyHawkLPM: fwVersion is : " + fwVersion);
      if(fwVersion.equals("0.00.001.00")) {
    	System.out.print(SkyHawkPowerManager.updateFW("fwu_skyhawk_0.00.001.01.bin"));
  	  } else if (fwVersion.equals("0.00.001.01")) {
    	System.out.print(SkyHawkPowerManager.updateFW("fwu_skyhawk_0.00.001.00.bin"));
   	  } else {
    	logger.log(Level.INFO, "skyHawkLPM: fwVersion Invalid : " + fwVersion);
      }
			
    } finally {
      channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
~~~~

---------------------------------

[Return to BLink services](blinkServices.md)