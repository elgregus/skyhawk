Services
============

Services will do the calls to BLink using the .proto procedures and messages.

---------------------------------

### Key concepts

The goal of a service is simply to act as a wrapper for the hardware platform.
Every call is done using a request, even if the request parameters are empty, you need to implement a request to use gRPC. <br> Replies are not mandatory as we will see in the examples below.

---------------------------------

### Example in Java

Here is a small example on how to implement such service for a client application to control simple GPIOs. 
This example will use java.utils.logging, these calls are optional.

The GPIOs have 2 procedures : 
- gpioSet 
- gpioGet

They also have 4 messages :
- Gpio_Set_Request
- Gpio_Set_Reply
- Gpio_Get_Request
- Gpio_Get_Reply

As you can see, every procedures have a reply and a request message. 
Even if a procedure does not have a value to return, error handing is done through the reply message.

#### Imports:

You will need many imports from the generated files from the .proto file. If you use eclipse, eclipse may not detect these imports and underline the import in red, but while compiling, maven will find the imports just fine.

~~~~{.java}
import java.util.logging.Level;
import java.util.logging.Logger;
import blink_grpc.Gpio_Get_Reply;
import blink_grpc.Gpio_Get_Request;
import blink_grpc.Gpio_ServiceGrpc;
import blink_grpc.Gpio_ServiceGrpc.Gpio_ServiceBlockingStub;
import blink_grpc.Gpio_Set_Request;
import blink_grpc.Gpio_Set_Reply;
import io.grpc.ManagedChannel;
import io.grpc.StatusRuntimeException;
~~~~

#### Constructor and private members

To be able to issue the calls, we need to declare a stub and initialize it in our constructor. In this example, we will be using a blocking stub.

~~~~{.java}
private final Gpio_ServiceBlockingStub blockingStub;

    public BLink_GPIO(ManagedChannel channel) {
        blockingStub = Gpio_ServiceGrpc.newBlockingStub(channel);
    }
~~~~

As simple as that, our service is created, we will see how to instantiate the ManagedChannel later in the example.

#### Defining our methods

Programming the methods is fairly easy, simply initialize a request, declare a reply (if needed) and then make the Grpc call. Here are two examples for our Set and Get.
** Replies are always need for error handling, only omit if you decide to not handle errors.

~~~~{.java}
    /** Set the GPIO value */
    public void setGPIO(String name, Boolean value) {

        Gpio_Set_Request request = Gpio_Set_Request.newBuilder().setName(name).setRequestedState(value).build();
		
		Gpio_Set_Reply reply;
        try {
            reply = blockingStub.gpioSet(request);
			if (reply.getError().getErrorCode() != 0) {
			    logger.log(Level.WARNING, "Oh no! " + reply.getError().getErrorCode() + " " + reply.getError().getErrorMessage());
			}
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            return;
        }
    }

    /** Get the GPIO value */
    public Boolean getGPIO(String name) {

        Gpio_Get_Request request = Gpio_Get_Request.newBuilder().setName(name).build();

        Gpio_Get_Reply response;
        try {
            response = blockingStub.gpioGet(request);
			if (reply.getError().getErrorCode() != 0) {
			    logger.log(Level.WARNING, "Oh no! " + reply.getError().getErrorCode() + " " + reply.getError().getErrorMessage());
			}
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed " + e.getStatus() + " " + e.getMessage());
            throw e;
        }
        return response.getValue();
    }
~~~~

As you can see, the request is built by calling the .newBuilder() on our request object, then we call .parameterName(parameter) to give a parameter to our request (if we have parameters) and finally we call .build().
The reply is simply returned when calling our Stub.method(request);

Error handling is done by getting the reply.getError() and getting the .getErrorCode() and .getErrorMessage. 
It is your choice how you handle these errors, in this simple example we are simply logging them.
See all errors [here](ErrorCodes.md).

Since the calls are blocking the thread will be blocked as long as the GRPC does not return. Consider using multiple threads because some calls can take a long time to reply.

---------------------------------

See section [Using Services](usingServices.md) to see how to use these implemented services.<br>
See section [BLink Services](blinkServices.md) to have a list of all BLink services and calls.<br>
See section [Error Codes](ErrorCodes.md) to have a list of possible error codes.
