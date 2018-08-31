GPIO Service
============

The GPIO service is used to control the multiple GPIOs present on SkyHawk.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### SkyHawk GPIOs

Here is the list of all GPIOs you can pass as parameters for the get and set methods.<br>
Note that you can't change a GPIO's direction

Name                | Direction | Description
------------------- | --------- | -----------
DIN1                | Input     | General input.
DIN2                | Input     | General input. 
DIN3                | Input     | General input.
DIN4                | Input     | General input.
DIN5                | Input     | General input.
DIN6                | Input     | General input.
DOUT1               | Output    | General output.
DOUT2               | Output    | General output.
DOUT3               | Output    | General output.
DOUT4               | Output    | General output.
LEDCELG             | Output    | Green color of the cellular led.
LEDCELR             | Output    | Red color of the cellular led.
LEDCELB             | Output    | Blue color of the cellular led.
LEDBT               | Output    | Bluetooth led.
LEDWIFI             | Output    | WiFi led.
LEDDATA             | Output    | Data led.
LEDGPS              | Output    | GPS led.
LPMIRQ              | Input     | IRQ from the LPM to the CPU

#### 

---------------------------------

### Java Methods

#### void setGPIO(String name, Boolean value):

The setGPIO method is used to set a specific GPIO to a '1' or a '0'.<br>
Only output GPIOs may be used with the setGPIO method, requests on input GPIOs will throw an exception.

- param  : 
         + String name   : Name of the GPIO we want to switch the state.
         + Boolean value : Value you want to set the GPIO to, can be either true (1) or false (0).
- return : None
  
#### Boolean getGPIO(String name):

The getGPIO method is used to get a specific GPIO value. this value is either '1' or '0'.<br>
Only input GPIOs may be used with the getGPIO method, requests on output GPIOs will throw an exception.

- param  : 
         + String name   : Name of the GPIO we want know the state.
	
- return :
         + Boolean : Value of the GPIO, can be either true (1) or false (0).

#### Void StartStreamGPIO(String name, String edge):

The StartStreamGPIO method is used as a notification callback for trigger event.
It will create a new data stream with the GPIO value (accessible in onNext() callback).
You only need to provide the GPIO name and the edge type.

- param  :
         + String name   : Name of the GPIO.
         + String edge   : Desired edge (rising, falling, both)

- return : None


#### void StopStreamGPIO(String name):

The StopStreamGPIO method is used to stop the GPIO stream.

- param  :
         + String name   : Name of the GPIO.
- return : None

---------------------------------

[Return to BLink services](blinkServices.md)
