package co.volight.utils

inline fun <T, R> Iterable<T>.forWith(acc: R, f: R.(v: T) -> Unit): R {
    this.forEach {
        acc.f(it)
    }
    return acc
}

inline fun <T, R> Iterable<T>.forWithIndexed(acc: R, f: R.(v: T, index: Int) -> Unit): R {
    this.forEachIndexed { i, v ->
        acc.f(v, i)
    }
    return acc
}
