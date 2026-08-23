package ch.yanick.bgr.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.reflect.KProperty

class LoggingUtils {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Logger {
        return if (thisRef != null) {
            LoggerFactory.getLogger(thisRef.javaClass)
        } else {
            LoggerFactory.getLogger(property.name)
        }
    }
}

fun logger(): LoggingUtils = LoggingUtils()