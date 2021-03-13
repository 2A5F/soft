package co.volight.soft.api.text

import co.volight.cell.Ref
import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Style
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting

inline fun styleOf(f: Ref<Style>.() -> Unit): Style {
    val style = Ref(Style.EMPTY)
    style.f()
    return style.value
}

fun Ref<Style>.color(formatting: Formatting) {
    this *= this.value.withColor(formatting)
}

fun Ref<Style>.color(textColor: TextColor) {
    this *= this.value.withColor(textColor)
}

fun Ref<Style>.bold() {
    this *= this.value.withBold(true)
}

fun Ref<Style>.bold(bold: Boolean) {
    this *= this.value.withBold(bold)
}

fun Ref<Style>.italic() {
    this *= this.value.withItalic(true)
}

fun Ref<Style>.italic(italic: Boolean) {
    this *= this.value.withItalic(italic)
}

fun Ref<Style>.hoverEvent(hoverEvent: HoverEvent) {
    this *= this.value.withHoverEvent(hoverEvent)
}

fun <T> Ref<Style>.hoverEvent(action: HoverEvent.Action<T>, obj: T) {
    this *= this.value.withHoverEvent(HoverEvent(action, obj))
}

fun Ref<Style>.clickEvent(clickEvent: ClickEvent) {
    this *= this.value.withClickEvent(clickEvent)
}

fun Ref<Style>.clickEvent(action: ClickEvent.Action, string: String) {
    this *= this.value.withClickEvent(ClickEvent(action, string))
}