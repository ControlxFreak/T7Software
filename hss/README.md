This directory contains the Home Station Software (HSS) source code and documentation.

To clean HSS:
ant clean

To compile HSS code:
ant

To run MainApp:
cd classes
java -classpath . app.MainApp

To run UAVServer:
cd classes
java -classpath . networking.server.UAVServer

To run TestUAVClient:
cd classes
java -classpath . networking.server.testing.TestUAVClient

To run TestHSSServer:
cd classes
java -classpath . networking.client.testing.TestHSSServer

To run UAVClientService:
cd classes
java -classpath . networking.client.UAVClientService
