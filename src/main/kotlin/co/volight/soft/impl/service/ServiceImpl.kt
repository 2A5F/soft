package co.volight.soft.impl.service

import co.volight.soft.Soft

internal const val logName = "[SoftAPI:Service]"

internal object ServiceImpl {
    private val services = mutableMapOf<Class<*>, ServiceData<*>>()

    fun <T: Any> isProvided(service: Class<T>): Boolean {
        @Suppress("UNCHECKED_CAST")
        val data = services[service] as ServiceData<T>?
        return data?.isProvided() ?: false
    }

    fun <T: Any> isProvidedDefault(service: Class<T>): Boolean {
        @Suppress("UNCHECKED_CAST")
        val data = services[service] as ServiceData<T>?
        return data?.isProvidedDefault() ?: false
    }

    fun <T: Any> has(service: Class<T>): Boolean {
        @Suppress("UNCHECKED_CAST")
        val data = services[service] as ServiceData<T>?
        return data?.has() ?: false
    }

    fun <T: Any> get(service: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        val data = services[service] as ServiceData<T>?
        return data?.get() ?: throw RuntimeException("Service does not exist")
    }

    fun <T: Any> tryGet(service: Class<T>): T? {
        @Suppress("UNCHECKED_CAST")
        val data = services[service] as ServiceData<T>?
        return data?.tryGet()
    }

    fun <T: Any> getData(service: Class<T>): ServiceData<T> {
        @Suppress("UNCHECKED_CAST")
        val data = services.getOrPut(service as Class<*>, { ServiceData<T>() }) as ServiceData<T>
        return data
    }

    fun <T: Any> provide(service: Class<T>, provide: T) {
        val data = getData(service)
        if (data.service != null) {
            Soft.LOGGER.warn("$logName Provided ${service.canonicalName} multiple times")
        }
        data.service = provide
        Soft.LOGGER.info("$logName Provided ${service.canonicalName}, now is ${provide::class.java.canonicalName}")
    }

    fun <T: Any> provideDefault(service: Class<T>, provide: T) {
        val data = getData(service)
        if (data.default != null) {
            Soft.LOGGER.error("$logName Provided default ${service.canonicalName} multiple times")
            throw RuntimeException("Provided default ${service.canonicalName} multiple times")
        }
        data.default = provide
        Soft.LOGGER.info("$logName Provided default ${service.canonicalName}, now is ${provide::class.java.canonicalName}")
    }
}

internal class ServiceData<T> {
    var default: T? = null
    var service: T? = null

    fun has() = (service ?: default) != null
    fun isProvided() = service != null
    fun isProvidedDefault() = default != null
    fun get() = service ?: default ?: throw RuntimeException("Service does not exist")
    fun tryGet() = service ?: default
}

