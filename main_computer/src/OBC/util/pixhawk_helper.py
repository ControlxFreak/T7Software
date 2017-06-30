#!/usr/bin/env python
# -*- coding: utf-8 -*-
from dronekit import connect, VehicleMode
import time

# Pixhawk class for handling all sorts of pixhawk related data stuff
class FlightController:
    
    # ------------------------------------------------- #
    # Initialize the constructor
    def __init__(self):
        print("Connecting to Pixhawk...")
        self.pixhawk = connect("/dev/ttyACM1", wait_ready=True)
        self.pixhawk.wait_ready('autopilot_version')
        
        self.pixhawk.parameters['ARMING_CHECK']=0
        self.pixhawk.armed = True
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
    
