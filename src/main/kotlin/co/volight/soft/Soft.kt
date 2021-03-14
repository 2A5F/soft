package co.volight.soft

import co.volight.soft.impl.events.PlayerLangInfo
import org.apache.logging.log4j.LogManager

typealias ModId = String

object Soft {
    internal const val id = "soft"
    internal const val logName = "[SoftAPI]"
    internal val LOGGER = LogManager.getLogger(id)!!
}

fun init() {
    regEvents()
}

fun regEvents() {
    PlayerLangInfo.regEvent()
}
