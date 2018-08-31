# Skyhawk Mezz Service

The Skyhawk Mezz Service is used to fetch and control the analog inputs from the mezzanine. This service also allows the user to get information on the mezzanine firmware and to launch an update.

The following analog inputs are available:

- Analog Input 1
- Analog Input 2
- Analog Input 3
- Analog Input 4
- Analog Differential 4-20 mA Input 1
- Analog Differential 4-20 mA Input 2

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

------

### Java Methods

Since gRPC always uses a system of request and reply, to simplify this documentation parameters in the request object have simply been tagged as param and return values in the reply object have been tagged as return values.

#### bool updateFW(String filePath):

The updateFw method is used to update the firmware in the mezzanine.<br>

- param  : 
  - String filePath   : Path of the new .bin file to update the firmware with.
- return :
  - Boolean : true for success, false for failure.

#### String getFWVersion():

The getFWVersion method is used to get the actual firmware version in the mezzanine.

- param  : None
- return : 
  - String : The string contains the firmware version, is maximum 30 characters long and is formatted as follow "0.00.000.00"

#### int getHWVersion():

The getHWVersion method is used to get the actual hardware version of the mezzanine board.

- param  : None
- return : 
  - int : The hardware version number	     

------

[Return to BLink services](blinkServices.md)