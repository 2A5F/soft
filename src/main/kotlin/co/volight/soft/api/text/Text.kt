package co.volight.soft.api.text

import net.minecraft.text.*

inline fun MutableText.style(f: StyleBuilder.() -> Unit) = this.setStyle(styleOf(f))
fun MutableText.style(style: Style) = this.setStyle(style)

operator fun MutableText.plus(other: Text): MutableText {
    return this.append(other)
}
operator fun MutableText.plus(other: String): MutableText {
    return this.append(LiteralText(other))
}

fun literal(str: String) = LiteralText(str)
fun String.toText() = LiteralText(this)

fun selector(pattern: String) = SelectorText(pattern)
fun keybind(key: String) = KeybindText(key)

fun texts(vararg texts: Text): MutableText {
    return texts.asIterable().fold("".toText() as MutableText) { acc, text -> acc + text }
}

fun Text.prefix(prefix: MutableText): MutableText {
    return prefix + this
}
fun Text.msgPrefix(prefix: MutableText): MutableText {
    return prefix + " " + this
}
