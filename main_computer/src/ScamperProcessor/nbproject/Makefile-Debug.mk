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
	${OBJECTDIR}/CameraClass.o \
	${OBJECTDIR}/CoreProcessor.o \
	${OBJECTDIR}/MissionParameters.o \
	${OBJECTDIR}/TCPClass.o \
	${OBJECTDIR}/main.o \
	${OBJECTDIR}/pugixml.o


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
LDLIBSOPTIONS=-L/usr/local/lib -Wl,-rpath,'/usr/local/lib' -lpthread `pkg-config --libs opencv`  

# Build Targets
.build-conf: ${BUILD_SUBPROJECTS}
	"${MAKE}"  -f nbproject/Makefile-${CND_CONF}.mk ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/scamperprocessor

${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/scamperprocessor: ${OBJECTFILES}
	${MKDIR} -p ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}
	${LINK.cc} -o ${CND_DISTDIR}/${CND_CONF}/${CND_PLATFORM}/scamperprocessor ${OBJECTFILES} ${LDLIBSOPTIONS}

${OBJECTDIR}/CameraClass.o: CameraClass.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab `pkg-config --cflags opencv` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/CameraClass.o CameraClass.cpp

${OBJECTDIR}/CoreProcessor.o: CoreProcessor.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab `pkg-config --cflags opencv` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/CoreProcessor.o CoreProcessor.cpp

${OBJECTDIR}/MissionParameters.o: MissionParameters.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab `pkg-config --cflags opencv` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/MissionParameters.o MissionParameters.cpp

${OBJECTDIR}/TCPClass.o: TCPClass.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab `pkg-config --cflags opencv` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/TCPClass.o TCPClass.cpp

${OBJECTDIR}/main.o: main.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab `pkg-config --cflags opencv` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/main.o main.cpp

${OBJECTDIR}/pugixml.o: pugixml.cpp
	${MKDIR} -p ${OBJECTDIR}
	${RM} "$@.d"
	$(COMPILE.cc) -g -I/usr/local/include/opencv2/calib3d -I/usr/local/include/opencv2/core -I/usr/local/include/opencv2/features2d -I/usr/local/include/opencv2/flann -I/usr/local/include/opencv2/highgui -I/usr/local/include/opencv2/imgcodecs -I/usr/local/include/opencv2/imgproc -I/usr/local/include/opencv2/ml -I/usr/local/include/opencv2/objdetect -I/usr/local/include/opencv2/photo -I/usr/local/include/opencv2/shape -I/usr/local/include/opencv2/stitching -I/usr/local/include/opencv2/superres -I/usr/local/include/opencv2/video -I/usr/local/include/opencv2/videoio -I/usr/local/include/opencv2/videostab `pkg-config --cflags opencv` -std=c++11  -MMD -MP -MF "$@.d" -o ${OBJECTDIR}/pugixml.o pugixml.cpp

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
