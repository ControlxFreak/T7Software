#
# Generated Makefile - do not edit!
#
# Edit the Makefile in the project folder instead (../Makefile). Each target
# has a -pre and a -post target defined where you can add customized code.
#
# This makefile implements configuration specific macros and targets.


# Environment
MKDIR=mkdir
CP=cp
GREP=grep
NM=nm
CCADMIN=CCadmin
RANLIB=ranlib
CC=gcc-5
CCC=g++-5
CXX=g++-5
FC=gfortran
AS=as

# Macros
CND_PLATFORM=GNU-Linux
CND_DLIB_EXT=so
CND_CONF=Debug
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/executive/Executive.o \
	${OBJECTDIR}/io/IOManager.o \
	${OBJECTDIR}/io/T7Messages.pb.o \
	${OBJECTDIR}/io/tcpacceptor.o \
	${OBJECTDIR}/io/tcpconnector.o \
	${OBJECTDIR}/io/tcpstream.o \
	${OBJECTDIR}/logger/LogManager.o \
	${OBJECTDIR}/main.o \
	${OBJECTDIR}/types/TSQueue.o \
	${OBJECTDIR}/watchdog/WatchDog.o


# C Compiler Flags
CFLAGS=

# CC Compiler Flags
CCFLAGS=
CXXFLAGS=

# Fortran Compiler Flags
FFLAGS=

# Assembler Flags
ASFLAGS=

# Link Libraries and Options
LDLIBSOPTIONS=-L/usr/lib/python2.7/config-x86_64-linux-gnu -lpthread `pkg-config --libs protobuf` -lpython2.7  

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc

${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}
	${LINK.cc} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc ${OBJECTFILES} ${LDLIBSOPTIONS}

${OBJECTDIR}/executive/Executive.o: executive/Executive.cpp
	${MKDIR} -p ${OBJECTDIR}/executive
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/executive/Executive.o executive/Executive.cpp

${OBJECTDIR}/io/IOManager.o: io/IOManager.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/IOManager.o io/IOManager.cpp

${OBJECTDIR}/io/T7Messages.pb.o: io/T7Messages.pb.cc
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/T7Messages.pb.o io/T7Messages.pb.cc

${OBJECTDIR}/io/tcpacceptor.o: io/tcpacceptor.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpacceptor.o io/tcpacceptor.cpp

${OBJECTDIR}/io/tcpconnector.o: io/tcpconnector.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpconnector.o io/tcpconnector.cpp

${OBJECTDIR}/io/tcpstream.o: io/tcpstream.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpstream.o io/tcpstream.cpp

${OBJECTDIR}/logger/LogManager.o: logger/LogManager.cpp
	${MKDIR} -p ${OBJECTDIR}/logger
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/logger/LogManager.o logger/LogManager.cpp

${OBJECTDIR}/main.o: main.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/main.o main.cpp

${OBJECTDIR}/types/TSQueue.o: types/TSQueue.cpp
	${MKDIR} -p ${OBJECTDIR}/types
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/types/TSQueue.o types/TSQueue.cpp

${OBJECTDIR}/watchdog/WatchDog.o: watchdog/WatchDog.cpp
	${MKDIR} -p ${OBJECTDIR}/watchdog
	${RM} "$@.d"
	$(COMPILE.cc) -g -DNPRINT_TO_FILE -DPRINT_TO_CONSOLE -Iio -Imain -Iexecutive -Imanagers -Iwatchdog -Itypes -Ilogger -I/usr/include/python2.7 `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/watchdog/WatchDog.o watchdog/WatchDog.cpp

# Subprojects
.build-subprojects:

# Clean Targets
.clean-conf: ${CLEAN_SUBPROJECTS}
	${RM} -r ${CND_BUILDDIR}/${CND_CONF}

# Subprojects
.clean-subprojects:

# Enable dependency checking
.dep.inc: .depcheck-impl

include .dep.inc
