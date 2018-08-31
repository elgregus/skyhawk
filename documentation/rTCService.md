RTC Service
============

The RTC service is used to interact with the on board RTC. It is used to keep track of time.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### Java Methods

#### long GetTime():

The GetTime method is used to get the time on the device (unix timestamp).

- param  : None
- return : 
         + long : The current unix timestamp.
		 
#### void SetTime(long unixTimestamp):

The SetTime method is used to set the time on the device (unix timestamp).

- param  : 
         + long unixTimestamp : The timestamp we want to set on the device.
- return : None

---------------------------------

[Return to BLink services](blinkServices.md)