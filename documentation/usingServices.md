Using Services
============

To use the GPIO, WiFi or any other peripheral from a user application, Brio developed BLink.
This page is dedicated to explaining how to use the services provided by BLink.

---------------------------------

### Key concepts

The goal of the service is simply to act as a wrapper to make calls to the hardware simpler.

---------------------------------

### Example in Java

Here is a small example on how to use the services in a client application. For this example, we will be using GPIOs
This example will use java.utils.logging, these calls are optional.

See how to implement a service [here](services.md).

The GPIOs have 2 methods :
- setGPIO
 + Set's a gpio to the desired state (ex : setGPIO("DOUT3", true))
- getGPIO
 + Get's a gpio state (ex : getGPIO("DIN3"))

#### Imports:

You will need a few imports from the generated files from the .proto file. If you use eclipse, eclipse may not detect these imports and underline the import in red, but while compiling, maven will find the imports just fine.

~~~~{.java}
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
~~~~

The ManagedChannel will be used to give a channel to our service so it can be used with Grpc.
The ManagedChannelBuilder is used to build the channel with the provided host and port.

#### Instantiating the Service:

The following code shows how to instantiate a service from anywhere, the complete code examples make use of constructors to instantiate the services. The results are the same.

~~~~{.java}
String host = "localhost";
int port = 50051;

final MannagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
final BLink_GPIO gpioService =  = new BLink_GPIO(channel);
~~~~

The channel is used to provide the location of the server we want to connect to make the Grpc calls.

#### Using the service methods:

To use the service methods, simply call the methods as you would do with any object.

~~~~{.java}
try { 
	// GPIO Set Example
	gpioService.setGPIO("DOUT3", true);
	gpioService.setGPIO("DOUT1", false);

	// GPIO Get Example
	Boolean value = blink.gpioService.getGPIO("DIN1");

	logger.log(Level.INFO, "GPIO value: " + value);
	} finally {
      
    }
~~~~

#### Clean-up:
Once you are done with a service you can clean it up.

~~~~{.java}
channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
~~~~

---------------------------------

See section [Services](services.md) to see how to create your own services (from a client perspective) and see section [BLink Services](blinkServices.md) to have a list of all BLink services and calls.