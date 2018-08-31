WiFi Service
============

The WiFi service is used to manage the WiFi connections.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Java Methods

#### java.util.List<AccessPoint> GetAccessPoints():

The GetAccessPoints method is used to get all access points nearby.<br>
Throws StatusRuntimeException.

- param  : None
- return : 
         + java.util.List<AccessPoint> : A list of all access points nearby

#### java.util.List<Connection> GetConnections():

The GetConnections method is used to get all connections points.<br>
Throws StatusRuntimeException.

- param  : None
- return : 
         + java.util.List<Connection> : A list of all connections points nearby
		 
#### void Connect(String id):

The Connect method is used to connect to a wifi connection using it's id.<br>
Throws StatusRuntimeException.

- param  :
         + String id : String containing the id of the connection we want to connect to.
- return : None

#### void AddConnection(String id, String ssid, String psk, Boolean persistent):

The AddConnection method is used to add a connection to the connection list.<br>
Throws StatusRuntimeException.

- param  :
         + String id : String containing the id of the connection we want to add.
		 + String ssid : String containing the ssid of the connection we want to add.
		 + String psk : String containing the pre-shared key of the connection we want to add
         + Boolean id : Is the connection persistent.
- return : None

####void AutoConnect(Boolean value):
The AutoConnect method is used to tell the WiFi if we want to automatically connect to a connection when not connected.<br>
Throws StatusRuntimeException.

- param  :
         + Boolean value : Enable auto connect (true) or disable auto connect (false).
- return : None

---------------------------------

### Objects

#### AccessPoint

- Members :
          + String ssid : ssid of the access point.
		  + uint signalQuality : Quality of the signal.
-Methods : None

#### Connection

- Members :
          + String id : id of the connection.
		  + String uuid : uuid of the connection.
-Methods : None

---------------------------------

[Return to BLink services](blinkServices.md)