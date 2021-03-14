package co.volight.soft.api.service

import co.volight.soft.impl.service.ServiceData
import co.volight.soft.impl.service.ServiceImpl

object Service {
    fun <T: Any> service(service: Class<T>) = ServiceHandle(ServiceImpl.getData(service))
    inline fun <reified T: Any> service() = service(T::class.java)

    fun <T: Any> has(service: Class<T>) = ServiceImpl.has(service)
    inline fun <reified T: Any> has() = has(T::class.java)

    fun <T: Any> get(service: Class<T>) = ServiceImpl.get(service)
    inline fun <reified T: Any> get() = get(T::class.java)

    fun <T: Any> tryGet(service: Class<T>) = ServiceImpl.tryGet(service)
    inline fun <reified T: Any> tryGet() = tryGet(T::class.java)

    fun <T: Any> isProvided(service: Class<T>) = ServiceImpl.isProvided(service)
    inline fun <reified T: Any> isProvided() = isProvided(T::class.java)

    fun <T: Any> isProvidedDefault(service: Class<T>) = ServiceImpl.isProvidedDefault(service)
    inline fun <reified T: Any> isProvidedDefault() = isProvidedDefault(T::class.java)

    fun <T: Any> provide(service: Class<T>, provide: T) = ServiceImpl.provide(service, provide)
    inline fun <reified T: Any> provide(provide: T) = provide(T::class.java, provide)

    fun <T: Any> provideDefault(service: Class<T>, provide: T) = ServiceImpl.provideDefault(service, provide)
    inline fun <reified T: Any> provideDefault(provide: T) = provideDefault(T::class.java, provide)
}

data class ServiceHandle<T> internal constructor (private val data: ServiceData<T>) {
    fun has() = data.has()
    fun isProvided() = data.isProvided()
    fun isProvidedDefault() = data.isProvidedDefault()
    fun get() = data.get()
    fun tryGet() = data.tryGet()
}
