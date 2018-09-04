Serial Port Service
============

The Serial Port Service is used to communicate with the serial port on the device. you may write or read on this serial port. Configuration is optional, since the server automatically does a default configuration.

The methods described and the example below will make direct use of gRPC calls.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Skyhawk Capabilities
Here is a list of capabilites for each configuration field

Field          | Type    | Values                                      
---------------|-------- | --------------------------------------------
Name           | String  | "BaseSerial" : DB9 connector <br> "MezzSerial" : Mezz connector                              
Baudrate       | int     | Any int                                      
Parity         | String  | "none", "odd", "even"                        
Character Size | int     | 5 to 9
Flow Control   | String  | "none", "software", "hardware"
Stop Bits      | String  | "one", "onepointfive", "two"

#### Default Configurations

**Baudrate :** 115200
**Parity :** none
**Character size :** 8
**Flow control :** none
**Stop bits :** one

---------------------------------

### Java Methods

Since gRPC always uses a system of request and reply, to simplify this documentation paramters in the request object have simply been tagged as param and return values in the reply object have been tagged as return values.

#### void Serial_Read(String deviceName, String mode):

The Read method is used to read on the serial port of the device.
It will create a stream of data that can be accessed in the : "onNext" callback.
You only need to provide the name of the device in the Request object
In the callback, you will need to get the gRPC reply, get your value and convert it to UTF-8 string.
See at the end of this page for a complete example.

- param  :
         + String deviceName : Name of the device we want to read from.
         + String mode : Mode to use with the specified device (ex: "RS232" or "RS485"). This parameter is optionnal if the specified device offers only a single mode.
- return : None
		 
#### void Serial_Write(String deviceName, bytes data, String mode):

Writes the bytes to the requested serial device. Bytes must be in UTF-8.

- param  : 
         + String deviceName : Name of the device we want to write to.
		 + bytes data : All the bytes that we want to write.
         + String mode : Mode to use with the specified device (ex: "RS232" or "RS485"). This parameter is optionnal if the specified device offers only a single mode.
- return : None

#### void Serial_Config(String deviceName, int baudrate, String parity, int charSize, String stopBits, String flowControl)

The Config method is used to configure a serial port on the device.

- param : 
        + String deviceName : Name of the device we want to configure.
		+ int baudrate : Baudrate of the device we want to configure.
		+ String parity : Parity of the device we want to configure.
		+ int charSize : Character size of the device we want to configure.
		+ String stopBits : Stop bit of the device we want to configure.
		+ String flowControl :
- return : None

#### void Serial_StopReading():

Sends a command to stop a stream of reading on a specified serial device.

- param  :
         + String devince Name : Name of the device we want to stop reading from.
- return : None

---------------------------------

[Return to BLink services](blinkServices.md)
