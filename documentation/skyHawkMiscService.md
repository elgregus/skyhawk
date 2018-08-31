Skyhawk Miscellaneous Service
============

The Skyhawk Miscellaneous Service is used to control various small components on the skyhawk board.

The methods described and the example below will make direct use of gRPC calls.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Java Methods

Since gRPC always uses a system of request and reply, to simplify this documentation paramters in the request object have simply been tagged as param and return values in the reply object have been tagged as return values.

#### void WiegandEN_OneWireDIS_Set(boolean requestedState):

Toogle between Wiegand and OneWire.

True      | false
--------- | ---------
Wiegand   | OneWire

- param  :
         + boolean resquestedState : State you want the Wiegand/OneWire port to be in.
- return : None
		 
#### boolean WiegandEN_OneWireDIS_Get()

Get the current state of the Wiegand/OneWire port.

True      | false
--------- | ---------
Wiegand   | OneWire

- param  : None
- return : 
         + boolean : Current state of the Wiegand/OneWire port.

#### void WWANAntennaEN_Set(boolean requestedState):

Toogle the WWANAntenna between internal and external antenna.

True      | false
--------- | ---------
external  | internal

- param  :
         + boolean resquestedState : State you want the WWANAntenna to be.
- return : None
		 
#### boolean WWANAntennaEN_Get()

Get the current state of the WWANAntenna.

True      | false
--------- | ---------
external  | internal

- param  : None
- return : 
         + boolean : Current state of the WWANAntenna.

#### void PoEEN_Set(boolean requestedState):

Toogle the PoE on/off.

True      | false
--------- | ---------
On        | Off

- param  :
         + boolean resquestedState : State you want the PoE to be.
- return : None
		 
#### boolean PoEEN_Get()

Get the current state of the PoE.

True      | false
--------- | ---------
On        | Off

- param  : None
- return : 
         + boolean : Current state of the PoE.

#### void MezzSerialMode_Set(string mode):

Set the mode of the Mezz serial port.

Name      | Value     | Description
--------- | --------- | -------------------------------------------
RS232     | "RS232"   | Set the Mezz serial port in RS232 mode
RS485     | "RS485"   | Set the Mezz serial port in RS485 mode
shutdown  | "SHDN"    | Shutdown the serial port of the Mezz

- param  :
         + string mode : Mode you want the Mezz serial port to be.
- return : None
		 
#### string MezzSerialMode_Get()

Get the mode of the Mezz serial port.

Name      | Value     | Description
--------- | --------- | -------------------------------------------
RS232     | "RS232"   | Set the Mezz serial port in RS232 mode
RS485     | "RS485"   | Set the Mezz serial port in RS485 mode
shutdown  | "SHDN"    | Shutdown the serial port of the Mezz

True      | false
--------- | ---------
On        | Off

- param  : None
- return : 
         + string : Current mode of the Mezz serial port.
         
#### boolean DetectMezz()

Detect if a Mezz is currently plugged-in. (Active low)

True             | false
---------------- | ---------
No Mezz on board | Mezz is connected to base board

- param  : None
- return : 
         + boolean : Is there a Mezz board connected to the base board (Active low).
         

---------------------------------

[Return to BLink services](blinkServices.md)
