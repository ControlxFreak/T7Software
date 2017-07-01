#!/usr/bin/env python
# -*- coding: utf-8 -*-
from dronekit import connect, VehicleMode
import socket
import time
import T7Messages_pb2
from google.protobuf.internal import encoder

# Pixhawk class for handling all sorts of pixhawk related data stuff
class FlightController:
    
    # ------------------------------------------------- #
    # Initialize the constructor
    def __init__(self):
        print("Connecting to Pixhawk...")
        self.pixhawk = connect("/dev/ttyACM0", wait_ready=True)
        self.pixhawk.wait_ready('autopilot_version')
        print("Pixhawk Connected!")

    # Make dem helper methods
    # ------------------------------------------------- #
    # Manager Stuff
    def get_heartbeat(self):
        return self.pixhawk.last_heartbeat

    def get_armed(self):
        return self.pixhawk.armed
    
    def get_state(self):
        return self.pixhawk.system_status.state

    def get_firmware_major(self):
        return self.pixhawk.version.major
    
    def get_firmware_minor(self):
        return self.pixhawk.version.minor
    
    def get_firmware_patch(self):
        return self.pixhawk.version.patch

    # ------------------------------------------------- #
    # Velocity Stuff
    def get_velx(self):
        return self.pixhawk.velocity[0]

    def get_vely(self):
        return self.pixhawk.velocity[1]
    
    def get_velz(self):
        return self.pixhawk.velocity[2]

    # ------------------------------------------------- #
    # Attitude stuff
    def get_roll(self):
        return self.pixhawk.attitude.roll

    def get_pitch(self):
        return self.pixhawk.attitude.pitch
    
    def get_yaw(self):
        return self.pixhawk.attitude.yaw

    # ------------------------------------------------- #
    # Altitude Stuff
    def get_altitude(self):
        return self.pixhawk.location.global_frame.alt
    
    # ------------------------------------------------- #
    # Battery Stuff
    def get_battery(self):
	return self.pixhawk.battery

# ---------------------------------------------------------#
# protobuf helper function
def deliminate_msg(packetMessage):
    serializedMessage = packetMessage.SerializeToString()
    delimiter = encoder._VarintBytes(len(serializedMessage))
    return delimiter + serializedMessage   

#----------------------------------------------------------#
#FC = FlightController()
pixhawk = connect("/dev/ttyACM0", wait_ready=True)
pixhawk.wait_ready('autopilot_version')
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect(('127.0.0.1',9001))

GM = T7Messages_pb2.GenericMessage()
GM.msgtype = 300
GM.time = 0

while True:
    print("sending message...")
    GM.pixhawk.velx = pixhawk.velocity[0]
    GM.pixhawk.vely = pixhawk.velocity[1]
    GM.pixhawk.velz = pixhawk.velocity[2]

    print "vel: %s" % pixhawk.velocity

    GM.pixhawk.roll = pixhawk.attitude.roll
    GM.pixhawk.pitch = pixhawk.attitude.pitch
    GM.pixhawk.yaw = pixhawk.attitude.yaw

    print "att: %s" % pixhawk.attitude
    
    GM.pixhawk.altitude = pixhawk.location.global_frame.alt

    print "alt: %s" % pixhawk.location.global_frame.alt
    
    GM.pixhawk.battery = 0;
    #GM.pixhawk.battery = float(FC.get_battery());
    s.send(deliminate_msg(GM))
    time.sleep(1)
