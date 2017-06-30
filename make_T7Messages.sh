#!/bin/bash

BASEDIR=$(dirname $0)
#echo "${BASEDIR}"
echo "Creating proto files..."
protoc --cpp_out="${BASEDIR}/main_computer/src/OBC/io/" --java_out="${BASEDIR}/hss/"  T7Messages.proto
echo "Success!"
