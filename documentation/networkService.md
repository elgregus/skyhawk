Network Service
============

The Network service is used to interact with the network manager.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Methods

#### int GetState():

The GetState method is used to get the actual state of the network subsystem.

- param  : None
- return :
         + int : Integer with the status of the network. (see Network Status table)



#### void AddConnection():

The AddConnection method is used to create a custom NetworkManager connection.

- param  : 
  - JsonObject : The JSON formatted DBus setting for the connection to be added, an example of a static ethernet connection is provided.
    Refer to the NetworkManager Settings Manual for more information:
    https://developer.gnome.org/NetworkManager/stable/nm-settings.html
    More information on the DBus variant types:
    https://dbus.freedesktop.org/doc/dbus-specification.html
  - Boolean: If true the added connection will be saved to disk in a persistent manner.
- return : None



#### Bool RemoveConnection():

The RemoveConnection method is used to remove an existing connection from the network subsystem.

- param  : 
  - String : The connectionID of the connection to be removed
- return :
  - Bool : Returns true if the connection was successfully removed



#### void GetConnections():

Get the connections that are currently registered with the NetworkManager

- param  : 
  - None
- return :
  - String List : Returns a list of connectionID associated with the existing connections.

---------------------------------

### Constants

#### Network Status table

Name                      | Value | Description
------------------------- | ----- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
NM_STATE_UNKNOWN          | 0     | Networking state is unknown. <br>This indicates a daemon error that makes it <br>unable to reasonably assess the state.
NM_STATE_ASLEEP 		  | 10    | Networking is not enabled, <br>the system is being suspended or resumed from suspend.
NM_STATE_DISCONNECTED	  | 20    | There is no active network connection.
NM_STATE_DISCONNECTING	  | 30    | Network connections are being cleaned up. <br>The applications should tear down their network sessions.
NM_STATE_CONNECTING 	  | 40    | A network connection is being started, <br>the applications should still make no attempts to connect the network.
NM_STATE_CONNECTED_LOCAL  | 50    | There is only local IPv4 and/or IPv6 connectivity, <br>but no default route to access the Internet.
NM_STATE_CONNECTED_SITE   | 60    | There is only site-wide IPv4 and/or IPv6 connectivity. <br>This means a default route is available, <br>but the Internet connectivity check did not succeed.
NM_STATE_CONNECTED_GLOBAL | 70    | There is global IPv4 and/or IPv6 Internet connectivity. <br>This means the Internet connectivity check succeeded.

---------------------------------

[Return to BLink services](blinkServices.md)