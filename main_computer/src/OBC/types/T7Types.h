/*------------------------------------------------------------------------------
Function Name: T7Types.h

--------------------------------------------------------------------------------
Inputs:
 * N/a
Outputs:
 * N/a
--------------------------------------------------------------------------------
Properties:
 * N/a
Methods:
 * N/a
--------------------------------------------------------------------------------
Lockheed Martin 
Engineering Leadership Development Program
Team 7
15 February 2017
Anthony Trezza
--------------------------------------------------------------------------------
Change Log
    04 June 2017 - t3 - Happy Birthday!
--------------------------------------------------------------------------------
 */

#ifndef T7TYPES_H
#define T7TYPES_H
// Properties //
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//
// Failure Codes:
//              criticalFailure: the system MUST be exited immediately.  Do not even try a restart.
//              serriousFailure: the system MUST be restarted immediately.
//              moderateFailure: the system SHOULD be restarted, but it probably could clean itself up first.
//             issolatedFailure: the system doesn't need a restart but something is failed.  Initiate isolated trouble shooting.  If that doesn't work, likely will go to moderate
//                 smallFailure: the section of code probably hiccuped. Use default settings or backup values.  Similar to isolated, but less sever.
//                    noFailure: Good to go!
//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------//

enum 
failureCodes {
    // Generic Failures
    critFail = -99,
    seriousFail,
    moderateFailure,
    issolatedFailure,
    smallFailure,
    RESTART,
    // Socket Failures
    SOCKET_FAILURE,
    CONNECT_FAIL,
    UNK_SOCK,
    socketDisconnected,
    //Successes
    noFailure = 0,
    //Socket Success
    socketConnected
};

// Define the thread keys
enum
threadKeys {
    // Sockets Threads //
    // Server //
    ServerSock,
    HeartSock,
    TerminateSock,
    ConfigSock,
    MoveCamSock,
    PixhawkSock,

    // Client //
    AccelSock,
    GyroSock,
    AltSock,
    AttSock,
    TempSock,
    BatSock,

    // Sensor Threads //
    WiFiKey,
    ThermalArrayKey
};

// Define the socket keys
enum
sockKeys {
    // INFO MESSAGE //
    RESPONSE = 0,
    HEARTBEAT = 1,
    TERMINATE = 2,

    // HSS -> UAV //
    CONFIG_DATA = 101,
    MOVE_CAMERA = 102,

    // UAV -> HSS //
    ACCEL = 200,
    GYRO = 201,
    ALTITUDE = 202,
    ATTITUDE = 203,
    TEMP = 204,
    BAT = 205,

    PIXHAWK = 300
};

// TimeToDieFlags
enum
timeToDieFlags {
    GLOBAL_SHUTDOWN = -99,
    EXECUTIVE_SHUTDOWN = -98,
    WATCHDOG_SHUTDOWN = -97,
    THERMAL_ARRAY_SHUTDOWN = 997,
    WIFI_SHUTDOWN = 998,
    SERVER_SHUTDOWN = 999,
    HEARTBEAT_SHUTDOWN = sockKeys::HEARTBEAT,
    TERMINATE_SHUTDOWN = sockKeys::TERMINATE,
    CONFIG_SHUTDOWN = sockKeys::CONFIG_DATA,
    MOVE_SHUTDOWN = sockKeys::MOVE_CAMERA,
    ACCEL_SHUTDOWN = sockKeys::ACCEL,
    GYRO_SHUTDOWN = sockKeys::GYRO,
    ALT_SHUTDOWN = sockKeys::ALTITUDE,
    ATT_SHUTDOWN = sockKeys::ATTITUDE,
    TEMP_SHUTDOWN = sockKeys::TEMP,
    BAT_SHUTDOWN = sockKeys::BAT,
    PIXHAWK_SHUTDOWN = sockKeys::PIXHAWK
};

// Define the terminate keys
enum
terminateKeys {
    SELF_TERMINATE = 0,
    SOFT_SHUTDOWN = 1,
    EMERGENCY_STOP = 2
};

// Define the toggle keys
enum
toggleKeys {
    TOGGLE_ACCEL = 0,
    TOGGLE_GYRO = 1,
    TOGGLE_ALTITUDE = 2,
    TOGGLE_ATTITUDE = 3,
    TOGGLE_TEMP = 4,
    TOGGLE_BAT = 5,
};

// Define the move camera commands
enum
camKeys {
    MOVE_UP = 0,
    MOVE_RIGHT = 1,
    MOVE_DOWN = 2,
    MOVE_LEFT = 3
};

#endif /* T7TYPES_H */

