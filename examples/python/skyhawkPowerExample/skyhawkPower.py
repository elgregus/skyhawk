from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.SkyHawkPowerManager_ServiceStub(channel)

if argv[1]=="setRegister":
	response = stub.SkyHawkPmgr_SetRegister(blink_pb2.SkyHawkPmgr_SetRegister_Request(regName=argv[2], value=int(argv[3])))
elif argv[1]=="getRegister":
	response = stub.SkyHawkPmgr_GetRegister(blink_pb2.SkyHawkPmgr_GetRegister_Request(regName=argv[2]))
	print (response.value)
elif argv[1]=="updateFW":
	stub.SkyHawkPmgr_UpdateFW(blink_pb2.SkyHawkPmgr_UpdateFW_Request(filePath=argv[2]))
elif argv[1]=="getFWVersion":
	response = stub.SkyHawkPmgr_GetFWVersion(blink_pb2.SkyHawkPmgr_GetFWVersion_Request())
	print (response.fwVersion)
elif argv[1]=="getHWVersion":
	response = stub.SkyHawkPmgr_GetHWVersion(blink_pb2.SkyHawkPmgr_GetHWVersion_Request())
	print (response.hwVersion)
else:
	print("Error, call format is skyhawkPMGRExample method [value]")
