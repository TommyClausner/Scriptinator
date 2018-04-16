#!/bin/bash

### BEGIN HEADER ###

### Scriptinator ###

# General information #
# file=none
# useqsub=true
# label=RunOnQsub
# ThreeLetter=ROQ

### Script ###

# Input Variables and Paths #
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
ScriptToRun=/Path/file.sh

# Output Variables and Paths #
OutputVar=none

# Qsub information #
jobtype=batch
walltime="24:00:00"
memory=64gb

# Misc Variables #
MiscVar=none

### END HEADER ###

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

qsub -q $jobtype-l walltime=$walltime,mem=$memory -F "$DIR ${@:1}" $ScriptToRun

PIDqsub=$(qstat | awk -F' ' '{print $1}' | tail -1)
statusqsub=$(qstat $PIDqsub | awk -F' ' '{print $5}' | tail -1)

while [ "$statusqsub" != "C" ]
do
sleep 1s
statusqsub=$(qstat $PIDqsub | awk -F' ' '{print $5}' | tail -1)

done