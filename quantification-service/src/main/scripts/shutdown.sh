#!/bin/sh
ID=`ps -ef | grep java | grep quantification-service.jar|awk '{print $2}'`
echo $ID
for id in $ID
do
  kill -9 $id
  echo "killed$id"
done