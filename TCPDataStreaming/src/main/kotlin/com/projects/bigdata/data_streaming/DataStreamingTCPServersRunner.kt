@file:JvmName("DataStreamingTCPServersRunner")
package com.projects.bigdata.data_streaming

import com.projects.bigdata.data_streaming.cassandra.CassandraManager
import com.projects.bigdata.data_streaming.utility.StreamingLineFactory
import com.projects.bigdata.data_streaming.utility.StreamingLineSupplier
import com.projects.bigdata.utility.StreamingLineType
import com.projects.bigdata.utility.*

import java.util.Arrays
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors

/**
 * It starts two TCP Servers, waits for the Spark Streaming application to connect and then sends data.
 * It stops after a configurable processing time.
 */
fun main(args: Array<String>) {
    fun getStreamingLineTypeFromCommandLine(args: Array<String>?): () -> String = if (args != null && !args.isEmpty())
        StreamingLineFactory.getStreamingLine(StreamingLineType.valueOf(args[0]))
    else
        StreamingLineSupplier::randomTrade
    with (getApplicationProperties("server.properties")) {
        val messageSendDelayMilliSeconds = getProperty("messageSendDelayMilliSeconds").toInt()
        val dataStreamServers = getProperty("port").split(",").asSequence().map { DataStreamingTCPServer(getStreamingLineTypeFromCommandLine(args), Integer.valueOf(it), messageSendDelayMilliSeconds) }.toList()
        val execService = Executors.newFixedThreadPool(dataStreamServers.size)
        dataStreamServers.asSequence().forEach { execService.execute(it) }
        sleep(TimeUnit.SECONDS, Integer.valueOf(getProperty("upTimeWindowSeconds")).toLong())
        dataStreamServers.stream().forEach(DataStreamingTCPServer::stop)
        shutdownExecutorService(execService, 1, TimeUnit.SECONDS)
    }
}