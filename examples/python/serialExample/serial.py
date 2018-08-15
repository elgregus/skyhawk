from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.SerialPort_ServiceStub(channel)

if argv[1]=="read":
	for response in stub.SerialPort_Read(blink_pb2.SerialPort_Read_Request(deviceName="BaseSerial", mode="RS232")):
		print (response.data)
elif argv[1]=="write":
	stub.SerialPort_Write(blink_pb2.SerialPort_Write_Request(deviceName="BaseSerial", data=argv[2], mode="RS232"))
else:
	print("Error, call format is serialExample method [data]")
