from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.Network_ServiceStub(channel)

if argv[1]=="get":
	response = stub.Network_GetState(blink_pb2.Network_GetState_Request())
	print (response.networkState)
else:
	print("Error, call format is networkExample method")


