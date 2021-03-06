# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: T7Messages.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='T7Messages.proto',
  package='T7',
  syntax='proto3',
  serialized_pb=_b('\n\x10T7Messages.proto\x12\x02T7\"\x9e\x06\n\x0eGenericMessage\x12\x0f\n\x07msgtype\x18\x01 \x01(\x05\x12\x0c\n\x04time\x18\x02 \x01(\x01\x12\x1e\n\x08response\x18\x03 \x01(\x0b\x32\x0c.T7.Response\x12 \n\theartbeat\x18\x04 \x01(\x0b\x32\r.T7.HeartBeat\x12 \n\tterminate\x18\x05 \x01(\x0b\x32\r.T7.Terminate\x12\"\n\nconfigdata\x18\x06 \x01(\x0b\x32\x0e.T7.ConfigData\x12\"\n\nmovecamera\x18\x07 \x01(\x0b\x32\x0e.T7.MoveCamera\x12*\n\x0ethermalrequest\x18\x0f \x01(\x0b\x32\x12.T7.ThermalRequest\x12\x18\n\x05\x61\x63\x63\x65l\x18\x08 \x01(\x0b\x32\t.T7.Accel\x12\x16\n\x04gyro\x18\t \x01(\x0b\x32\x08.T7.Gyro\x12\x1e\n\x08\x61ltitude\x18\n \x01(\x0b\x32\x0c.T7.Altitude\x12\x1e\n\x08\x61ttitude\x18\x0b \x01(\x0b\x32\x0c.T7.Attitude\x12\x16\n\x04temp\x18\x0c \x01(\x0b\x32\x08.T7.Temp\x12\x18\n\x03\x62\x61t\x18\r \x01(\x0b\x32\x0b.T7.Battery\x12\x19\n\x04head\x18\x0e \x01(\x0b\x32\x0b.T7.Heading\x12,\n\x0fthermalresponse\x18\x10 \x01(\x0b\x32\x13.T7.ThermalResponse\x12\x16\n\x04wifi\x18\x11 \x01(\x0b\x32\x08.T7.WiFi\x12\x1c\n\x07pixhawk\x18\x12 \x01(\x0b\x32\x0b.T7.Pixhawk\"\xf1\x01\n\x07MsgType\x12\x0c\n\x08RESPONSE\x10\x00\x12\r\n\tHEARTBEAT\x10\x01\x12\r\n\tTERMINATE\x10\x02\x12\x0f\n\x0b\x43ONFIG_DATA\x10\x65\x12\x0f\n\x0bMOVE_CAMERA\x10\x66\x12\x13\n\x0fTHERMAL_REQUEST\x10g\x12\n\n\x05\x41\x43\x43\x45L\x10\xc8\x01\x12\t\n\x04GYRO\x10\xc9\x01\x12\r\n\x08\x41LTITUDE\x10\xca\x01\x12\r\n\x08\x41TTITUDE\x10\xcb\x01\x12\t\n\x04TEMP\x10\xcc\x01\x12\x08\n\x03\x42\x41T\x10\xcd\x01\x12\t\n\x04HEAD\x10\xce\x01\x12\x15\n\x10THERMAL_RESPONSE\x10\xcf\x01\x12\t\n\x04WIFI\x10\xd0\x01\x12\x0c\n\x07PIXHAWK\x10\xac\x02\"\x1e\n\x08Response\x12\x12\n\nroger_that\x18\x01 \x01(\x08\"\x1a\n\tHeartBeat\x12\r\n\x05\x61live\x18\x01 \x01(\x08\"j\n\tTerminate\x12\x14\n\x0cterminateKey\x18\x01 \x01(\x05\"G\n\rTerminateKeys\x12\x11\n\rselfTerminate\x10\x00\x12\x10\n\x0csoftShutdown\x10\x01\x12\x11\n\remergencyStop\x10\x02\"\xc7\x01\n\nConfigData\x12\x11\n\tconfigKey\x18\x01 \x01(\x05\"\xa5\x01\n\nToggleKeys\x12\x0f\n\x0btoggleAccel\x10\x00\x12\x0e\n\ntoggleGyro\x10\x01\x12\x12\n\x0etoggleAltitude\x10\x02\x12\x12\n\x0etoggleAttitude\x10\x03\x12\x0e\n\ntoggleTemp\x10\x04\x12\r\n\ttoggleBat\x10\x05\x12\x0f\n\x0btoggleArray\x10\x06\x12\x0e\n\ntoggleHead\x10\x07\x12\x0e\n\ntoggleWifi\x10\x08\"R\n\nMoveCamera\x12\x10\n\x08\x61rrowKey\x18\x01 \x01(\x05\"2\n\tArrowKeys\x12\x06\n\x02UP\x10\x00\x12\t\n\x05RIGHT\x10\x01\x12\x08\n\x04\x44OWN\x10\x02\x12\x08\n\x04LEFT\x10\x03\"!\n\x0eThermalRequest\x12\x0f\n\x07request\x18\x01 \x01(\x08\"(\n\x05\x41\x63\x63\x65l\x12\t\n\x01x\x18\x01 \x01(\x01\x12\t\n\x01y\x18\x02 \x01(\x01\x12\t\n\x01z\x18\x03 \x01(\x01\"\'\n\x04Gyro\x12\t\n\x01x\x18\x01 \x01(\x01\x12\t\n\x01y\x18\x02 \x01(\x01\x12\t\n\x01z\x18\x03 \x01(\x01\"\x17\n\x08\x41ltitude\x12\x0b\n\x03\x61lt\x18\x01 \x01(\x01\"4\n\x08\x41ttitude\x12\x0c\n\x04roll\x18\x01 \x01(\x01\x12\r\n\x05pitch\x18\x02 \x01(\x01\x12\x0b\n\x03yaw\x18\x03 \x01(\x01\"\x14\n\x04Temp\x12\x0c\n\x04temp\x18\x01 \x01(\x01\"\x1a\n\x07\x42\x61ttery\x12\x0f\n\x07percent\x18\x01 \x01(\x01\"\x1a\n\x07Heading\x12\x0f\n\x07heading\x18\x01 \x01(\x01\"#\n\x0fThermalResponse\x12\x10\n\x08response\x18\x01 \x01(\x01\"&\n\x04WiFi\x12\x10\n\x08strength\x18\x01 \x01(\x01\x12\x0c\n\x04\x66req\x18\x02 \x01(\x01\"\x80\x01\n\x07Pixhawk\x12\x0c\n\x04velx\x18\x01 \x01(\x01\x12\x0c\n\x04vely\x18\x02 \x01(\x01\x12\x0c\n\x04velz\x18\x03 \x01(\x01\x12\x0c\n\x04roll\x18\x04 \x01(\x01\x12\r\n\x05pitch\x18\x05 \x01(\x01\x12\x0b\n\x03yaw\x18\x06 \x01(\x01\x12\x10\n\x08\x61ltitude\x18\x07 \x01(\x01\x12\x0f\n\x07\x62\x61ttery\x18\x08 \x01(\x01\x62\x06proto3')
)



_GENERICMESSAGE_MSGTYPE = _descriptor.EnumDescriptor(
  name='MsgType',
  full_name='T7.GenericMessage.MsgType',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='RESPONSE', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='HEARTBEAT', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='TERMINATE', index=2, number=2,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='CONFIG_DATA', index=3, number=101,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='MOVE_CAMERA', index=4, number=102,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='THERMAL_REQUEST', index=5, number=103,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='ACCEL', index=6, number=200,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='GYRO', index=7, number=201,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='ALTITUDE', index=8, number=202,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='ATTITUDE', index=9, number=203,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='TEMP', index=10, number=204,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='BAT', index=11, number=205,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='HEAD', index=12, number=206,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='THERMAL_RESPONSE', index=13, number=207,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='WIFI', index=14, number=208,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='PIXHAWK', index=15, number=300,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=582,
  serialized_end=823,
)
_sym_db.RegisterEnumDescriptor(_GENERICMESSAGE_MSGTYPE)

_TERMINATE_TERMINATEKEYS = _descriptor.EnumDescriptor(
  name='TerminateKeys',
  full_name='T7.Terminate.TerminateKeys',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='selfTerminate', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='softShutdown', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='emergencyStop', index=2, number=2,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=920,
  serialized_end=991,
)
_sym_db.RegisterEnumDescriptor(_TERMINATE_TERMINATEKEYS)

_CONFIGDATA_TOGGLEKEYS = _descriptor.EnumDescriptor(
  name='ToggleKeys',
  full_name='T7.ConfigData.ToggleKeys',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='toggleAccel', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='toggleGyro', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='toggleAltitude', index=2, number=2,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='toggleAttitude', index=3, number=3,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='toggleTemp', index=4, number=4,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='toggleBat', index=5, number=5,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='toggleArray', index=6, number=6,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='toggleHead', index=7, number=7,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='toggleWifi', index=8, number=8,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=1028,
  serialized_end=1193,
)
_sym_db.RegisterEnumDescriptor(_CONFIGDATA_TOGGLEKEYS)

_MOVECAMERA_ARROWKEYS = _descriptor.EnumDescriptor(
  name='ArrowKeys',
  full_name='T7.MoveCamera.ArrowKeys',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='UP', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='RIGHT', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='DOWN', index=2, number=2,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='LEFT', index=3, number=3,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=1227,
  serialized_end=1277,
)
_sym_db.RegisterEnumDescriptor(_MOVECAMERA_ARROWKEYS)


_GENERICMESSAGE = _descriptor.Descriptor(
  name='GenericMessage',
  full_name='T7.GenericMessage',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='msgtype', full_name='T7.GenericMessage.msgtype', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='time', full_name='T7.GenericMessage.time', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='response', full_name='T7.GenericMessage.response', index=2,
      number=3, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='heartbeat', full_name='T7.GenericMessage.heartbeat', index=3,
      number=4, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='terminate', full_name='T7.GenericMessage.terminate', index=4,
      number=5, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='configdata', full_name='T7.GenericMessage.configdata', index=5,
      number=6, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='movecamera', full_name='T7.GenericMessage.movecamera', index=6,
      number=7, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='thermalrequest', full_name='T7.GenericMessage.thermalrequest', index=7,
      number=15, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='accel', full_name='T7.GenericMessage.accel', index=8,
      number=8, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='gyro', full_name='T7.GenericMessage.gyro', index=9,
      number=9, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='altitude', full_name='T7.GenericMessage.altitude', index=10,
      number=10, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='attitude', full_name='T7.GenericMessage.attitude', index=11,
      number=11, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='temp', full_name='T7.GenericMessage.temp', index=12,
      number=12, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='bat', full_name='T7.GenericMessage.bat', index=13,
      number=13, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='head', full_name='T7.GenericMessage.head', index=14,
      number=14, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='thermalresponse', full_name='T7.GenericMessage.thermalresponse', index=15,
      number=16, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='wifi', full_name='T7.GenericMessage.wifi', index=16,
      number=17, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='pixhawk', full_name='T7.GenericMessage.pixhawk', index=17,
      number=18, type=11, cpp_type=10, label=1,
      has_default_value=False, default_value=None,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
    _GENERICMESSAGE_MSGTYPE,
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=25,
  serialized_end=823,
)


_RESPONSE = _descriptor.Descriptor(
  name='Response',
  full_name='T7.Response',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='roger_that', full_name='T7.Response.roger_that', index=0,
      number=1, type=8, cpp_type=7, label=1,
      has_default_value=False, default_value=False,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=825,
  serialized_end=855,
)


_HEARTBEAT = _descriptor.Descriptor(
  name='HeartBeat',
  full_name='T7.HeartBeat',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='alive', full_name='T7.HeartBeat.alive', index=0,
      number=1, type=8, cpp_type=7, label=1,
      has_default_value=False, default_value=False,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=857,
  serialized_end=883,
)


_TERMINATE = _descriptor.Descriptor(
  name='Terminate',
  full_name='T7.Terminate',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='terminateKey', full_name='T7.Terminate.terminateKey', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
    _TERMINATE_TERMINATEKEYS,
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=885,
  serialized_end=991,
)


_CONFIGDATA = _descriptor.Descriptor(
  name='ConfigData',
  full_name='T7.ConfigData',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='configKey', full_name='T7.ConfigData.configKey', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
    _CONFIGDATA_TOGGLEKEYS,
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=994,
  serialized_end=1193,
)


_MOVECAMERA = _descriptor.Descriptor(
  name='MoveCamera',
  full_name='T7.MoveCamera',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='arrowKey', full_name='T7.MoveCamera.arrowKey', index=0,
      number=1, type=5, cpp_type=1, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
    _MOVECAMERA_ARROWKEYS,
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1195,
  serialized_end=1277,
)


_THERMALREQUEST = _descriptor.Descriptor(
  name='ThermalRequest',
  full_name='T7.ThermalRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='request', full_name='T7.ThermalRequest.request', index=0,
      number=1, type=8, cpp_type=7, label=1,
      has_default_value=False, default_value=False,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1279,
  serialized_end=1312,
)


_ACCEL = _descriptor.Descriptor(
  name='Accel',
  full_name='T7.Accel',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='x', full_name='T7.Accel.x', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='y', full_name='T7.Accel.y', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='z', full_name='T7.Accel.z', index=2,
      number=3, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1314,
  serialized_end=1354,
)


_GYRO = _descriptor.Descriptor(
  name='Gyro',
  full_name='T7.Gyro',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='x', full_name='T7.Gyro.x', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='y', full_name='T7.Gyro.y', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='z', full_name='T7.Gyro.z', index=2,
      number=3, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1356,
  serialized_end=1395,
)


_ALTITUDE = _descriptor.Descriptor(
  name='Altitude',
  full_name='T7.Altitude',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='alt', full_name='T7.Altitude.alt', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1397,
  serialized_end=1420,
)


_ATTITUDE = _descriptor.Descriptor(
  name='Attitude',
  full_name='T7.Attitude',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='roll', full_name='T7.Attitude.roll', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='pitch', full_name='T7.Attitude.pitch', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='yaw', full_name='T7.Attitude.yaw', index=2,
      number=3, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1422,
  serialized_end=1474,
)


_TEMP = _descriptor.Descriptor(
  name='Temp',
  full_name='T7.Temp',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='temp', full_name='T7.Temp.temp', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1476,
  serialized_end=1496,
)


_BATTERY = _descriptor.Descriptor(
  name='Battery',
  full_name='T7.Battery',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='percent', full_name='T7.Battery.percent', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1498,
  serialized_end=1524,
)


_HEADING = _descriptor.Descriptor(
  name='Heading',
  full_name='T7.Heading',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='heading', full_name='T7.Heading.heading', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1526,
  serialized_end=1552,
)


_THERMALRESPONSE = _descriptor.Descriptor(
  name='ThermalResponse',
  full_name='T7.ThermalResponse',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='response', full_name='T7.ThermalResponse.response', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1554,
  serialized_end=1589,
)


_WIFI = _descriptor.Descriptor(
  name='WiFi',
  full_name='T7.WiFi',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='strength', full_name='T7.WiFi.strength', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='freq', full_name='T7.WiFi.freq', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1591,
  serialized_end=1629,
)


_PIXHAWK = _descriptor.Descriptor(
  name='Pixhawk',
  full_name='T7.Pixhawk',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='velx', full_name='T7.Pixhawk.velx', index=0,
      number=1, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='vely', full_name='T7.Pixhawk.vely', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='velz', full_name='T7.Pixhawk.velz', index=2,
      number=3, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='roll', full_name='T7.Pixhawk.roll', index=3,
      number=4, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='pitch', full_name='T7.Pixhawk.pitch', index=4,
      number=5, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='yaw', full_name='T7.Pixhawk.yaw', index=5,
      number=6, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='altitude', full_name='T7.Pixhawk.altitude', index=6,
      number=7, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
    _descriptor.FieldDescriptor(
      name='battery', full_name='T7.Pixhawk.battery', index=7,
      number=8, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=1632,
  serialized_end=1760,
)

_GENERICMESSAGE.fields_by_name['response'].message_type = _RESPONSE
_GENERICMESSAGE.fields_by_name['heartbeat'].message_type = _HEARTBEAT
_GENERICMESSAGE.fields_by_name['terminate'].message_type = _TERMINATE
_GENERICMESSAGE.fields_by_name['configdata'].message_type = _CONFIGDATA
_GENERICMESSAGE.fields_by_name['movecamera'].message_type = _MOVECAMERA
_GENERICMESSAGE.fields_by_name['thermalrequest'].message_type = _THERMALREQUEST
_GENERICMESSAGE.fields_by_name['accel'].message_type = _ACCEL
_GENERICMESSAGE.fields_by_name['gyro'].message_type = _GYRO
_GENERICMESSAGE.fields_by_name['altitude'].message_type = _ALTITUDE
_GENERICMESSAGE.fields_by_name['attitude'].message_type = _ATTITUDE
_GENERICMESSAGE.fields_by_name['temp'].message_type = _TEMP
_GENERICMESSAGE.fields_by_name['bat'].message_type = _BATTERY
_GENERICMESSAGE.fields_by_name['head'].message_type = _HEADING
_GENERICMESSAGE.fields_by_name['thermalresponse'].message_type = _THERMALRESPONSE
_GENERICMESSAGE.fields_by_name['wifi'].message_type = _WIFI
_GENERICMESSAGE.fields_by_name['pixhawk'].message_type = _PIXHAWK
_GENERICMESSAGE_MSGTYPE.containing_type = _GENERICMESSAGE
_TERMINATE_TERMINATEKEYS.containing_type = _TERMINATE
_CONFIGDATA_TOGGLEKEYS.containing_type = _CONFIGDATA
_MOVECAMERA_ARROWKEYS.containing_type = _MOVECAMERA
DESCRIPTOR.message_types_by_name['GenericMessage'] = _GENERICMESSAGE
DESCRIPTOR.message_types_by_name['Response'] = _RESPONSE
DESCRIPTOR.message_types_by_name['HeartBeat'] = _HEARTBEAT
DESCRIPTOR.message_types_by_name['Terminate'] = _TERMINATE
DESCRIPTOR.message_types_by_name['ConfigData'] = _CONFIGDATA
DESCRIPTOR.message_types_by_name['MoveCamera'] = _MOVECAMERA
DESCRIPTOR.message_types_by_name['ThermalRequest'] = _THERMALREQUEST
DESCRIPTOR.message_types_by_name['Accel'] = _ACCEL
DESCRIPTOR.message_types_by_name['Gyro'] = _GYRO
DESCRIPTOR.message_types_by_name['Altitude'] = _ALTITUDE
DESCRIPTOR.message_types_by_name['Attitude'] = _ATTITUDE
DESCRIPTOR.message_types_by_name['Temp'] = _TEMP
DESCRIPTOR.message_types_by_name['Battery'] = _BATTERY
DESCRIPTOR.message_types_by_name['Heading'] = _HEADING
DESCRIPTOR.message_types_by_name['ThermalResponse'] = _THERMALRESPONSE
DESCRIPTOR.message_types_by_name['WiFi'] = _WIFI
DESCRIPTOR.message_types_by_name['Pixhawk'] = _PIXHAWK
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

GenericMessage = _reflection.GeneratedProtocolMessageType('GenericMessage', (_message.Message,), dict(
  DESCRIPTOR = _GENERICMESSAGE,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.GenericMessage)
  ))
_sym_db.RegisterMessage(GenericMessage)

Response = _reflection.GeneratedProtocolMessageType('Response', (_message.Message,), dict(
  DESCRIPTOR = _RESPONSE,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Response)
  ))
_sym_db.RegisterMessage(Response)

HeartBeat = _reflection.GeneratedProtocolMessageType('HeartBeat', (_message.Message,), dict(
  DESCRIPTOR = _HEARTBEAT,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.HeartBeat)
  ))
_sym_db.RegisterMessage(HeartBeat)

Terminate = _reflection.GeneratedProtocolMessageType('Terminate', (_message.Message,), dict(
  DESCRIPTOR = _TERMINATE,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Terminate)
  ))
_sym_db.RegisterMessage(Terminate)

ConfigData = _reflection.GeneratedProtocolMessageType('ConfigData', (_message.Message,), dict(
  DESCRIPTOR = _CONFIGDATA,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.ConfigData)
  ))
_sym_db.RegisterMessage(ConfigData)

MoveCamera = _reflection.GeneratedProtocolMessageType('MoveCamera', (_message.Message,), dict(
  DESCRIPTOR = _MOVECAMERA,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.MoveCamera)
  ))
_sym_db.RegisterMessage(MoveCamera)

ThermalRequest = _reflection.GeneratedProtocolMessageType('ThermalRequest', (_message.Message,), dict(
  DESCRIPTOR = _THERMALREQUEST,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.ThermalRequest)
  ))
_sym_db.RegisterMessage(ThermalRequest)

Accel = _reflection.GeneratedProtocolMessageType('Accel', (_message.Message,), dict(
  DESCRIPTOR = _ACCEL,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Accel)
  ))
_sym_db.RegisterMessage(Accel)

Gyro = _reflection.GeneratedProtocolMessageType('Gyro', (_message.Message,), dict(
  DESCRIPTOR = _GYRO,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Gyro)
  ))
_sym_db.RegisterMessage(Gyro)

Altitude = _reflection.GeneratedProtocolMessageType('Altitude', (_message.Message,), dict(
  DESCRIPTOR = _ALTITUDE,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Altitude)
  ))
_sym_db.RegisterMessage(Altitude)

Attitude = _reflection.GeneratedProtocolMessageType('Attitude', (_message.Message,), dict(
  DESCRIPTOR = _ATTITUDE,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Attitude)
  ))
_sym_db.RegisterMessage(Attitude)

Temp = _reflection.GeneratedProtocolMessageType('Temp', (_message.Message,), dict(
  DESCRIPTOR = _TEMP,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Temp)
  ))
_sym_db.RegisterMessage(Temp)

Battery = _reflection.GeneratedProtocolMessageType('Battery', (_message.Message,), dict(
  DESCRIPTOR = _BATTERY,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Battery)
  ))
_sym_db.RegisterMessage(Battery)

Heading = _reflection.GeneratedProtocolMessageType('Heading', (_message.Message,), dict(
  DESCRIPTOR = _HEADING,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Heading)
  ))
_sym_db.RegisterMessage(Heading)

ThermalResponse = _reflection.GeneratedProtocolMessageType('ThermalResponse', (_message.Message,), dict(
  DESCRIPTOR = _THERMALRESPONSE,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.ThermalResponse)
  ))
_sym_db.RegisterMessage(ThermalResponse)

WiFi = _reflection.GeneratedProtocolMessageType('WiFi', (_message.Message,), dict(
  DESCRIPTOR = _WIFI,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.WiFi)
  ))
_sym_db.RegisterMessage(WiFi)

Pixhawk = _reflection.GeneratedProtocolMessageType('Pixhawk', (_message.Message,), dict(
  DESCRIPTOR = _PIXHAWK,
  __module__ = 'T7Messages_pb2'
  # @@protoc_insertion_point(class_scope:T7.Pixhawk)
  ))
_sym_db.RegisterMessage(Pixhawk)


# @@protoc_insertion_point(module_scope)
