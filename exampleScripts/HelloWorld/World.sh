#!/bin/bash

### BEGIN HEADER ###

### Scriptinator ###

# General information #
# file=none
# useqsub=false
# label=WorldScript
# shortLabel=Wrld

### Script ###

# Input Variables and Paths #
InputVar="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/Greetings.txt"
AddThis=" World"

# Output Variables and Paths #
OutputVar=$InputVar

# Qsub information #
jobtype=batch
walltime="24:00:00"
memory=64gb

# Misc Variables #
FurtherAdd="!"

### END HEADER ###

echo $AddThis
echo $AddThis$FurtherAdd>>$OutputVar