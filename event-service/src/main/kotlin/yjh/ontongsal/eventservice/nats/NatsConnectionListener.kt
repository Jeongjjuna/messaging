package yjh.ontongsal.eventservice.nats

import io.github.oshai.kotlinlogging.KotlinLogging
import io.nats.client.Connection
import io.nats.client.ConnectionListener

private val log = KotlinLogging.logger {}

class NatsConnectionListener : ConnectionListener {

    override fun connectionEvent(conn: Connection, type: ConnectionListener.Events) {
        when (type) {
            ConnectionListener.Events.CONNECTED ->
                log.info { "NATS Connected" }

            ConnectionListener.Events.DISCONNECTED ->
                log.info { "NATS Disconnected" }

            ConnectionListener.Events.RECONNECTED ->
                log.info { "NATS Reconnected" }

            ConnectionListener.Events.CLOSED ->
                log.info { "NATS Connection Closed" }

            else ->
                log.info { "NATS Event: $type" }
        }
    }
}