package co.volight.soft

import co.volight.soft.impl.events.PlayerLangInfo
import org.apache.logging.log4j.LogManager

object Soft {
    const val id = "soft"
    const val logName = "[SoftAPI]"
    val LOGGER = LogManager.getLogger(id)!!
}

fun init() {
    regEvents()
}

fun regEvents() {
    PlayerLangInfo.regEvent()
}
