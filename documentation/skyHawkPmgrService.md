SkyHawkPowerManager Service
============

The SkyHawkPowerManager service is used to get and set registers in the LPM, it is also used to send updates to the module.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------


### Java Methods

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
param  : 

- ​    + String filePath   : Path of the new .bin file to update the firmware with.
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

### LPM Register Status LPM v1.00.010

| Registers                                   | Status (Todo, In progress, Done) |
| ------------------------------------------- | -------------------------------- |
| [0x00-0x0E] - Firmware Version              | Done                             |
| 0x0F - Hardware Version                     | Done                             |
| 0x10 - CPU Wake Event Mask                  | Done (bit2 not implemented)      |
| 0x11 - CPU Wake Event Delay                 | Done                             |
| 0x20 - Digital Inputs Polarity              | Done                             |
| 0x21 - Power Control                        | Done                             |
| 0x22 - GPS External Antenna Control         | Done                             |
| 0x30 - VBAT Low Threshold                   | Done                             |
| 0x31 - VBAT Shelf Mode                      | Done                             |
| 0x32 - VIN RTC Mode (VIN Lost)              | Done                             |
| 0x33 - VIN Normal (Low Voltage) Mode        | Done                             |
| 0x40 - VIN Hysteresis                       | Done                             |
| 0x41 - VBAT Hysteresis                      | Done                             |
| 0x50 - IRQ Enable                           | Done                             |
| 0x51 - Low Power Mode Request Timeout       | Done                             |
| 0x52 - CPU Cold Boot Keep Alive Timeout     | Done                             |
| 0x53 - CPU Keep Alive Timeout               | Done                             |
| 0x54 - Power Loss Warning Delay             | Done                             |
| 0x55 - Power Loss Shutdown Timeout          | Done                             |
| 0x60 - Last Wake Event Status               | Done (bit2 not implemented)      |
| 0x61 - VIN Voltage Status                   | Done                             |
| 0x62 - VBAT Voltage Status                  | Done                             |
| 0x63 - Power Status                         | Done                             |
| 0x64 - GPS External Antenna Status          | Done                             |
| 0x65 - IRQ Status                           | Done                             |
| 0x70 - Firmware Update Commands             | Done                             |
| [0x71-0x72] - Firmware Update Start Address | Done                             |
| [0x73-0x74] - Firmware Update End Address   | Done                             |
| 0x75 - Firmware Update Status               | Done                             |
| [0x80-0x84] - RTC Time                      | Done                             |

 

------



### LPM Registers

Registers can be found in the `LowPowerManagerRegisters_v1_00_010.pdf` file provided with this documentation.

 

------

### LPM Usage Example (v1.00.010)

Here is an example on how to send the SkyHawk modem into low power mode using the LPM registers:


##### Use-case #1 Low power request with VIN

Conditions:

CPU Wake Event Mask =0x0001 (reg.: 0x10)
VIN Normal low voltage=12000 (reg.: 0x33)

Sequence:

- Adjust Vin to 12V.
- Wait CPU wake-up.
- Send Request Low Power (reg.: 0x21 bit 1).
- Wait CPU shutdown delay (default 5s).
- LPM will be in low power mode when red led is turn off.

##### Use-case #2 Low power request when VIN loss

Conditions:

- CPU Wake Event Mask =0x0001 (reg.: 0x10)
- VIN Normal low voltage=12000 (reg.: 0x33)
- Power Loss Warning Delay=5(reg: 0x54)
- Power Loss Shutdown Timeout=5 (reg: 0x55)
- A installed battery

Sequence:

- Adjust Vin to 12V.
- Wait CPU wake-up.
- Remove 12V on VIN
- You will receive a IRQ for VIN loss 5s later.
- Wait an extra 5 seconds after IRQ for CPU shutdown.
- LPM will be in low power mode when red led is turn off.

---------------------------------

[Return to BLink services](blinkServices.md)
