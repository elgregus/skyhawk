from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.RTC_ServiceStub(channel)

if argv[1]=="set":
	stub.RTC_SetTime(blink_pb2.RTC_SetTime_Request(unixTimestamp=int(argv[2])))
elif argv[1]=="get":
	response = stub.RTC_GetTime(blink_pb2.RTC_GetTime_Request())
	print (response.unixTimestamp)
else:
	print("Error, call format is rtcExample method [time]")

