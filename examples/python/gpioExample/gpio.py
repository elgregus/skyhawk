from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.Gpio_ServiceStub(channel)

if argv[1]=="set":
	stub.Gpio_Set(blink_pb2.Gpio_Set_Request(name=argv[2], requestedState=int(argv[3])))
elif argv[1]=="get":
	response = stub.Gpio_Get(blink_pb2.Gpio_Get_Request(name=argv[2]))
	print (response.value)
else:
	print("Error, call format is gpioExample method gpioname [requestedState]")
