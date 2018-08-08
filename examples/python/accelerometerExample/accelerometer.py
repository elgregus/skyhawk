from sys import argv

import grpc

import blink_pb2
import blink_pb2_grpc

channel = grpc.insecure_channel('localhost:50051')
stub = blink_pb2_grpc.Accelerometer_ServiceStub(channel)

if argv[1]=="getSamples":
	response = stub.Accelerometer_Get_Samples(blink_pb2.Accelerometer_Get_Samples_Request())
	print (response.xAxis)
	print (response.yAxis)
	print (response.zAxis)
	print (response.timestamp)
elif argv[1]=="setDataRate":
	stub.Accelerometer_Set_Data_Rate(blink_pb2.Accelerometer_Set_Data_Rate_Request(dataRate=int(argv[2])))
elif argv[1]=="setRange":
	stub.Accelerometer_Set_Range(blink_pb2.Accelerometer_Set_Range_Request(axisRange=int(argv[2])))
elif argv[1]=="setTrigger":
	stub.Accelerometer_Set_Trigger(blink_pb2.Accelerometer_Set_Trigger_Request(thsX=int(argv[2]), thsY=int(argv[3]), thsZ=int(argv[4]), dur=int(argv[5]), conf=int(argv[6])))
elif argv[1]=="startMonitoring":
	stub.Accelerometer_Start_Monitoring(blink_pb2.Accelerometer_Start_Monitoring_Request())
elif argv[1]=="startTrigger":
	stub.Accelerometer_Start_Trigger(blink_pb2.Accelerometer_Start_Trigger_Request())
elif argv[1]=="stopMonitoring":
	stub.Accelerometer_Stop_Monitoring(blink_pb2.Accelerometer_Stop_Monitoring_Request())
elif argv[1]=="stopTrigger":
	stub.Accelerometer_Stop_Trigger(blink_pb2.Accelerometer_Stop_Trigger_Request())
elif argv[1]=="loopSamples":
	while True:
		response = stub.Accelerometer_Get_Samples(blink_pb2.Accelerometer_Get_Samples_Request())
		print (response.xAxisList)
		print (response.yAxisList)
		print (response.zAxisList)
		print (response.timestampList)
		time.sleep(0.5)
else:
	print("Error, call format is skyhawkMiscExample interface [action] [value]")
