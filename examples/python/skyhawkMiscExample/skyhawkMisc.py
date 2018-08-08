from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.SkyhawkMisc_ServiceStub(channel)

if argv[1]=="detectMezz":
	response = stub.DetectMezz(blink_pb2.DetectMezz_Request())
	print (response.value)
elif argv[1]=="mezzSerialMode":
	if argv[2]=="set":
		stub.MezzSerialMode_Set(blink_pb2.MezzSerialMode_Set_Request(mode=argv[3]))
	elif argv[2]=="get":
		response = stub.MezzSerialMode_Get(blink_pb2.MezzSerialMode_Get_Request())
		print (response.value)
	else:
		print("Error, call format is skyhawkMiscExample interface [action] [value]")
elif argv[1]=="wiegandEnable":
	if argv[2]=="set":
		stub.WiegandEN_OneWireDIS_Set(blink_pb2.WiegandEN_OneWireDIS_Set_Request(requestedState=int(argv[3])))
	elif argv[2]=="get":
		response = stub.WiegandEN_OneWireDIS_Get(blink_pb2.WiegandEN_OneWireDIS_Get_Request())
		print (response.value)
	else:
		print("Error, call format is skyhawkMiscExample interface [action] [value]")
elif argv[1]=="poeEnable":
	if argv[2]=="set":
		stub.PoEEN_Set(blink_pb2.PoEEN_Set_Request(requestedState=int(argv[3])))
	elif argv[2]=="get":
		response = stub.PoEEN_Get(blink_pb2.PoEEN_Get_Request())
		print (response.value)
	else:
		print("Error, call format is skyhawkMiscExample interface [action] [value]")
else:
	print("Error, call format is skyhawkMiscExample interface [action] [value]")



