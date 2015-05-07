#!/bin/bash
tr '[:upper:]' '[:lower:]' < $1 | awk '!a[$0]++' | tr -d '[:punct:]' | grep -v '^\s*$'> $1"_final" 
