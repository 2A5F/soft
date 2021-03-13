package co.volight.cell

open class Out<T>(protected var content: T? = null) {
    open val value = content!!

    open fun get(): T {
        return value
    }
    open infix fun set(v: T) {
        content = v
    }
    open operator fun not(): T {
        return value
    }
    open operator fun timesAssign(v: T) {
        content = v
    }
}

open class Ref<T>(v: T) : Out<T>(v)
