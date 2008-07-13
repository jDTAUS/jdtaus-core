#!/bin/sh
SVN=`which svn`
BASE="https://jdtaus.svn.sourceforge.net/svnroot/jdtaus/jdtaus-core"

$SVN diff $BASE/trunk $BASE/branches/jdtaus-core-1.x

$SVN diff $BASE/jdtaus-resource-mojo/trunk \
        $BASE/jdtaus-resource-mojo/branches/jdtaus-resource-mojo-1.x

$SVN diff $BASE/jdtaus-container-mojo/trunk \
        $BASE/jdtaus-container-mojo/branches/jdtaus-container-mojo-1.x

$SVN diff $BASE/jdtaus-core-api/trunk \
        $BASE/jdtaus-core-api/branches/jdtaus-core-api-1.x

$SVN diff $BASE/jdtaus-core-application-logger/trunk \
        $BASE/jdtaus-core-application-logger/branches/jdtaus-core-application-logger-1.x

$SVN diff $BASE/jdtaus-core-client-container/trunk \
        $BASE/jdtaus-core-client-container/branches/jdtaus-core-client-container-1.x

$SVN diff $BASE/jdtaus-core-servlet-container/trunk \
	$BASE/jdtaus-core-servlet-container/branches/jdtaus-core-servlet-container-1.x

$SVN diff $BASE/jdtaus-core-task-monitor/trunk \
        $BASE/jdtaus-core-task-monitor/branches/jdtaus-core-task-monitor-1.x

$SVN diff $BASE/jdtaus-core-it/trunk \
        $BASE/jdtaus-core-it/branches/jdtaus-core-it-1.x

$SVN diff $BASE/jdtaus-core-jdk-logging/trunk \
        $BASE/jdtaus-core-jdk-logging/branches/jdtaus-core-jdk-logging-1.x

$SVN diff $BASE/jdtaus-core-memory-manager/trunk \
        $BASE/jdtaus-core-memory-manager/branches/jdtaus-core-memory-manager-1.x

$SVN diff $BASE/jdtaus-core-spi/trunk \
        $BASE/jdtaus-core-spi/branches/jdtaus-core-spi-1.x

$SVN diff $BASE/jdtaus-core-utilities/trunk \
        $BASE/jdtaus-core-utilities/branches/jdtaus-core-utilities-1.x

$SVN diff $BASE/jdtaus-core-messages/trunk \
	$BASE/jdtaus-core-messages/branches/jdtaus-core-messages-1.x

$SVN diff $BASE/jdtaus-core-commons-logging/trunk \
        $BASE/jdtaus-core-commons-logging/branches/jdtaus-core-commons-logging-1.x

$SVN diff $BASE/jdtaus-core-log4j-logging/trunk \
        $BASE/jdtaus-core-log4j-logging/branches/jdtaus-core-log4j-logging-1.x

$SVN diff $BASE/jdtaus-core-jdk14-executor/trunk \
        $BASE/jdtaus-core-jdk14-executor/branches/jdtaus-core-jdk14-executor-1.x

$SVN diff $BASE/jdtaus-core-entity-resolver/trunk \
        $BASE/jdtaus-core-entity-resolver/branches/jdtaus-core-entity-resolver-1.x

