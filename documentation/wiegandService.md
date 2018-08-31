Wiegand Service
============

The wiegand Service is used to read the wiegand port on the Mezz of the device. This port is read only and all configuration is handled by the server.

Important note : The Wiegand port is plexed with the OneWire port. You need to [use the SkyHawkMisc Service](skyHawkMiscService.md) to select which mode you want to use.

The methods described and the example below will make direct use of gRPC calls.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Java Methods

Since gRPC uses a system of request and reply, paramters in the request object have been tagged as param and return values in the reply object have been tagged as return values.

#### void Wiegand_Read():

The Read method is used to read on the wiegand port of the device.
It will create a stream of data that can be accessed in the : "onNext" callback.
You only need to provide the name of the device in the Request object
In the callback, you will need to get the gRPC reply, get your value (as a long).
See at the end of this page for a complete example.

- param  :
         + String deviceName : Name of the wiegand device we want to read from.
- return : None

#### void Serial_StopReading():

Sends a command to stop a stream of reading on a specified wiegand device.

- param  :
         + String devince Name : Name of the device we want to stop reading from.
- return : None


---------------------------------

[Return to BLink services](blinkServices.md)
