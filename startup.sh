#!/bin/bash

APP_NAME="casealot-backend"
JAR_FILE="/jenkins/workspace/casealot-backend/build/libs/shop-0.0.1-SNAPSHOT.jar"
PID_FILE="/home/ubuntu/${APP_NAME}.pid"
LOG_FILE="/home/ubuntu/log/${APP_NAME}.log"
SPRING_PROFILE=develop


start() {
    if [ -f $PID_FILE ]; then
        echo "$APP_NAME is already running with PID: $(cat $PID_FILE)"
        exit 1
    fi

    if [ -n "$SPRING_PROFILE" ]; then
        nohup java -jar $JAR_FILE --spring.profiles.active=$SPRING_PROFILE > $LOG_FILE 2>&1 &
    else
        nohup java -jar $JAR_FILE > $LOG_FILE 2>&1 &
    fi

    echo $! > $PID_FILE
    echo "$APP_NAME started with PID: $(cat $PID_FILE)"
}

stop() {
    if [ ! -f $PID_FILE ]; then
        echo "$APP_NAME is not running"
        exit 1
    fi

    kill $(cat $PID_FILE)
    rm $PID_FILE
    echo "$APP_NAME stopped"
}

case $1 in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
        start
        ;;
    profile)
        if [ -z "$2" ]; then
            echo "Usage: $0 profile <spring-profile>"
            exit 1
        fi
        SPRING_PROFILE=$2
        start
        ;;
    *)
        echo "Usage: $0 {start|stop|restart|profile}"
        exit 1
        ;;
esac
