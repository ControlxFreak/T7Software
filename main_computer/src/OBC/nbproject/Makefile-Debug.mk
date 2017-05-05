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
CC=gcc
CCC=g++
CXX=g++
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
	${OBJECTDIR}/managers/ThreadManager.o \
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
LDLIBSOPTIONS=-lpthread -lboost_atomic -lboost_atomic -lboost_chrono -lboost_chrono -lboost_context -lboost_context -lboost_coroutine -lboost_coroutine -lboost_date_time -lboost_date_time -lboost_exception -lboost_filesystem -lboost_filesystem -lboost_graph -lboost_graph -lboost_graph_parallel -lboost_graph_parallel -lboost_iostreams -lboost_iostreams -lboost_locale -lboost_locale -lboost_log -lboost_log -lboost_log_setup -lboost_log_setup -lboost_math_c99 -lboost_math_c99 -lboost_math_c99f -lboost_math_c99f -lboost_math_c99l -lboost_math_c99l -lboost_math_tr1 -lboost_math_tr1 -lboost_math_tr1f -lboost_math_tr1f -lboost_math_tr1l -lboost_math_tr1l -lboost_mpi -lboost_mpi -lboost_mpi_python-py27 -lboost_mpi_python-py27 -lboost_mpi_python-py35 -lboost_mpi_python-py35 -lboost_mpi_python -lboost_mpi_python -lboost_prg_exec_monitor -lboost_prg_exec_monitor -lboost_program_options -lboost_program_options -lboost_python-py27 -lboost_python-py27 -lboost_python-py35 -lboost_python-py35 -lboost_python -lboost_python -lboost_random -lboost_random -lboost_regex -lboost_regex -lboost_serialization -lboost_serialization -lboost_signals -lboost_signals -lboost_system -lboost_system -lboost_test_exec_monitor -lboost_thread -lboost_thread -lboost_timer -lboost_timer -lboost_unit_test_framework -lboost_unit_test_framework -lboost_wave -lboost_wave -lboost_wserialization -lboost_wserialization `pkg-config --libs protobuf`  

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc

${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}
	${LINK.cc} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc ${OBJECTFILES} ${LDLIBSOPTIONS}

${OBJECTDIR}/executive/Executive.o: executive/Executive.cpp
	${MKDIR} -p ${OBJECTDIR}/executive
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/executive/Executive.o executive/Executive.cpp

${OBJECTDIR}/io/IOManager.o: io/IOManager.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/IOManager.o io/IOManager.cpp

${OBJECTDIR}/io/T7Messages.pb.o: io/T7Messages.pb.cc
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/T7Messages.pb.o io/T7Messages.pb.cc

${OBJECTDIR}/io/tcpacceptor.o: io/tcpacceptor.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpacceptor.o io/tcpacceptor.cpp

${OBJECTDIR}/io/tcpconnector.o: io/tcpconnector.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpconnector.o io/tcpconnector.cpp

${OBJECTDIR}/io/tcpstream.o: io/tcpstream.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpstream.o io/tcpstream.cpp

${OBJECTDIR}/logger/LogManager.o: logger/LogManager.cpp
	${MKDIR} -p ${OBJECTDIR}/logger
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/logger/LogManager.o logger/LogManager.cpp

${OBJECTDIR}/main.o: main.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/main.o main.cpp

${OBJECTDIR}/managers/ThreadManager.o: managers/ThreadManager.cpp
	${MKDIR} -p ${OBJECTDIR}/managers
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/managers/ThreadManager.o managers/ThreadManager.cpp

${OBJECTDIR}/types/TSQueue.o: types/TSQueue.cpp
	${MKDIR} -p ${OBJECTDIR}/types
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/types/TSQueue.o types/TSQueue.cpp

${OBJECTDIR}/watchdog/WatchDog.o: watchdog/WatchDog.cpp
	${MKDIR} -p ${OBJECTDIR}/watchdog
	${RM} "$@.d"
	$(COMPILE.cc) -g -DPRINT_TO_CONSOLE -DPRINT_TO_FILE -Ibuild -Idist -Iio -Imain -Iexecutive -Iexternal/boost_1_63_0 -Imanagers -Iwatchdog -Itypes -Ilogger `pkg-config --cflags protobuf` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/watchdog/WatchDog.o watchdog/WatchDog.cpp

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
