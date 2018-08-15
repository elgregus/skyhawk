from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.Wiegand_ServiceStub(channel)

if argv[1]=="read":
	for response in stub.Wiegand_Read(blink_pb2.Wiegand_Read_Request()):
		print (response.data)
else:
	print("Error, call format is wiegandExample method")
