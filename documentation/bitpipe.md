BitPipe Service
============

The BitPipe service is used to interact with the bitpipe manager.

This implementation does not handles errors. See [services](services.md) to learn how to implement error handling in this wrapper.

---------------------------------

### RPCs

#### BitPipe_GetStatus

The GetStatus method is used to get the status of the BitPipe.
It's also used to get certain configurations of the BitPipe Manager.

The reply is in a JSON form with the following mapping:

- `net_prefix` : The subnet mask
- `modem_state` : The current state of the modem
- `net_dns_s` : Secondary DNS
- `net_dns_p` : Primary DNS
- `phone` : Phone number (if available)
- `iccid` : SIM card identification number (in circuit card identifier)
- `imei` : International Mobile Equipment Identity
- `net_netmask` : Same as net_prefix in another format
- `imsi` : International mobile subscriber identity
- `net_gateway` : Gateway of the cellular modem
- `net_ip` : Currently assigned IP address


#### BitPipe_GetSignalInfo

The GetSignalInfo method is used to get signal information from the BitPipe.

The reply is in a JSON form with the following mapping:

- `ecno` : Ec/No is the RSCP divided by the RSSI
- `operator` : Current cellular provider
- `rscp` : Received signal code power
- `cell_id` : Cellular tower ID that the BitPipe is currently connected
- `rssi` : Received signal strength indication in dBm
- `type` : 3G / 4G
- `mnc` : Mobile Network Code
- `rsrq` : Reference Signal Received Quality
- `rsrp` : Reference Signal Received Power
- `lac` : Local Area Code
- `mcc` : Mobile Country Code

#### BitPipe_SetAPN

Set the APN
- param  : 
         + String apn : The apn to configure the BitPipe manager with.

#### BitPipe_GetAPN

Get the APN

#### BitPipe_Connect

Connect the BitPipe

#### BitPipe_Disconnect

Disconnect the BitPipe

[Return to BLink services](blinkServices.md)