This directory contains the Home Station Software (HSS) source code and documentation.

To compile HSS code:
ant clean
ant build

To run UAVServer:
cd classes
java -classpath . networking.UAVServer

To run TestUAVCient:
cd classes
java -classpath . networking.testing.TestUAVClient
