from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.Wifi_ServiceStub(channel)

if argv[1]=="getAccessPoints":
	response = stub.Wifi_GetAccessPoints(blink_pb2.Wifi_GetAccessPoints_Request())
	print (response.accessPoint)
elif argv[1]=="getConnections":
	response = stub.Wifi_GetConnections(blink_pb2.Wifi_GetConnections_Request())
	print (response.wifiConnection)
elif argv[1]=="connect":
	stub.Wifi_Connect(blink_pb2.Wifi_Connect_Request(id=argv[2]))
elif argv[1]=="addConnection":
	stub.Wifi_AddConnection(blink_pb2.Wifi_AddConnection_Request(id=argv[2], ssid=argv[3], psk=argv[4], persistent=bool(argv[5])))
elif argv[1]=="autoConnect":
	stub.Wifi_SetAutoConnect(blink_pb2.Wifi_SetAutoConnect_Request(value=bool(argv[2])))
else:
	print("Error, call format is wifiExample method [value(s)]")

