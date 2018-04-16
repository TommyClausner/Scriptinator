#!/bin/bash

### BEGIN HEADER ###

### Scriptinator ###

# General information #
# file=none
# useqsub=false
# label=Hello
# shortLabel=HeLa

### Script ###

# Input Variables and Paths #
Language=English
saywhat=$(cat $( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/Languages.txt | grep $Language | awk  '{print $2}')

# Output Variables and Paths #
OutputVar="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )/Greetings.txt"

# Qsub information #
jobtype=batch
walltime="24:00:00"
memory=64gb

# Misc Variables #
MiscVar=none

### END HEADER ###
rm $OutputVar
touch $OutputVar
echo $saywhat
echo $saywhat > $OutputVar