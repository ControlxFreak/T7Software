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
CND_CONF=Release
CND_DISTDIR=dist
CND_BUILDDIR=build

# Include project Makefile
include Makefile

# Object Directory
OBJECTDIR=${CND_BUILDDIR}/${CND_CONF}/${CND_PLATFORM}

# Object Files
OBJECTFILES= \
	${OBJECTDIR}/_ext/b9fadef4/Person.pb.o \
	${OBJECTDIR}/algorithms/Algorithms.o \
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
LDLIBSOPTIONS=-lpthread

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc

${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}
	${LINK.cc} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/obc ${OBJECTFILES} ${LDLIBSOPTIONS}

${OBJECTDIR}/_ext/b9fadef4/Person.pb.o: ../../../../../Desktop/testproto/Person.pb.cc
	${MKDIR} -p ${OBJECTDIR}/_ext/b9fadef4
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/_ext/b9fadef4/Person.pb.o ../../../../../Desktop/testproto/Person.pb.cc

${OBJECTDIR}/algorithms/Algorithms.o: algorithms/Algorithms.cpp
	${MKDIR} -p ${OBJECTDIR}/algorithms
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/algorithms/Algorithms.o algorithms/Algorithms.cpp

${OBJECTDIR}/executive/Executive.o: executive/Executive.cpp
	${MKDIR} -p ${OBJECTDIR}/executive
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/executive/Executive.o executive/Executive.cpp

${OBJECTDIR}/io/IOManager.o: io/IOManager.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/IOManager.o io/IOManager.cpp

${OBJECTDIR}/io/T7Messages.pb.o: io/T7Messages.pb.cc
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/T7Messages.pb.o io/T7Messages.pb.cc

${OBJECTDIR}/io/tcpacceptor.o: io/tcpacceptor.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpacceptor.o io/tcpacceptor.cpp

${OBJECTDIR}/io/tcpconnector.o: io/tcpconnector.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpconnector.o io/tcpconnector.cpp

${OBJECTDIR}/io/tcpstream.o: io/tcpstream.cpp
	${MKDIR} -p ${OBJECTDIR}/io
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/io/tcpstream.o io/tcpstream.cpp

${OBJECTDIR}/logger/LogManager.o: logger/LogManager.cpp
	${MKDIR} -p ${OBJECTDIR}/logger
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/logger/LogManager.o logger/LogManager.cpp

${OBJECTDIR}/main.o: main.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/main.o main.cpp

${OBJECTDIR}/managers/ThreadManager.o: managers/ThreadManager.cpp
	${MKDIR} -p ${OBJECTDIR}/managers
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/managers/ThreadManager.o managers/ThreadManager.cpp

${OBJECTDIR}/types/TSQueue.o: types/TSQueue.cpp
	${MKDIR} -p ${OBJECTDIR}/types
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/types/TSQueue.o types/TSQueue.cpp

${OBJECTDIR}/watchdog/WatchDog.o: watchdog/WatchDog.cpp
	${MKDIR} -p ${OBJECTDIR}/watchdog
	${RM} "$@.d"
	$(COMPILE.cc) -O2 -DPRINT_TO_CONSOLE -Ialgorithms -Ibuild -Idist -Iinfo -Iio -Imain -Iexec -Iexternal/boost_1_63_0 -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab -Iexternal/pugi -std=c++11 -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/watchdog/WatchDog.o watchdog/WatchDog.cpp

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
