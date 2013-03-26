VehicleData.co Client - Java
============================

This is the source for the Java client for the VehicleData.co API, a service which provides various pieces of vehicle-related data. It currently is focused on cars, but is intended to be expanded to other vehicles types.

For more information, please visit http://www.vehicledata.co.

Usage Notes
-----------
You will first need and API key and secret to access the API. Sign up here: http://www.vehicledata.co/signup/

Once you have those, plug them into the vdcDemo.java file where the VehicleDataCo class is constructed.

To compile and run, this library requires the JSON.simple libraries from http://code.google.com/p/json-simple/. To set up, download the .jar file (json-simple-1.1.1.jar is current as of this writing) into the same directory directory as VehicleDataCo.java and vdcDemo.java and compile with:
	javac -cp json-simple-1.1.1.jar:. vdcDemo.java

You can then run with:
	java -cp json-simple-1.1.1.jar:. vdcDemo

Copyright 2013 Blacktop Ventures LLC