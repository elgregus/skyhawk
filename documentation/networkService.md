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

### Example

This is a short example only covering the basic operations.<br>
For a more robust and detailed implementation, go see the example project file BLink_Network_example.java and BLink_Network.java

~~~~{.java}
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BLink_Network_example {

  String host = "localhost";
  int port = 50051;

  public void testGPIO() {
    final MannagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
    final BLink_Network networkService =  = new BLink_Network(channel);
    
	try {
      // get the network state
      int state = networkService.getState();

	  System.out.print(state);
			
    } finally {
      channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
  }
}
~~~~

---------------------------------

[Return to BLink services](blinkServices.md)