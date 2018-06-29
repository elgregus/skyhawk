Blink                  {#mainpage}
============

BLink is a cross-platform & cross-language RPC based framework.
It allows the user to control and configure the hardware remotely or locally.

[Services](blinkServices.md)<br>
[Setting up a Java IDE with Eclipse](Setting-up_IDE.md)<br>
[Using Services](usingServices.md)<br>
[Creating service](services.md)<br>
[Error Handling](errorHandling.md) <br>

---------------------------------

### Important Note

This documentation is meant to explain how to build a client application to use the BLink framework.<br>
It does not contain the detailed documentation about the internals of the BLink framework.

---------------------------------

### Client Dependencies

- Google's ProtocolBuffer 3.5
- Google's gRPC 1.8
- BLink Protocol File (blink.proto) v0.02


---------------------------------

### Components

#### Protocol Files:

Protocol files are *Google Protobuf* .proto files that list the available procedure calls and messages that can be done between BLink and the client application. <br> This .proto file is provided by BrioWireless and will need to be inside your project path under "src/main/proto".

#### Services:

Services are files programmed by the client application that will do the calls to the server using the .proto procedures and messages.