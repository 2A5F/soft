package co.volight.soft.api.text

import net.minecraft.text.ClickEvent
import net.minecraft.text.HoverEvent
import net.minecraft.text.Style
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting

class StyleBuilder(var style: Style) {
    val BLACK = Formatting.BLACK
    val DARK_BLUE = Formatting.DARK_BLUE
    val DARK_GREEN= Formatting.DARK_GREEN
    val DARK_AQUA= Formatting.DARK_AQUA
    val DARK_RED= Formatting.DARK_RED
    val DARK_PURPLE= Formatting.DARK_PURPLE
    val GOLD= Formatting.GOLD
    val GRAY= Formatting.GRAY
    val DARK_GRAY= Formatting.DARK_GRAY
    val BLUE= Formatting.BLUE
    val GREEN= Formatting.GREEN
    val AQUA= Formatting.AQUA
    val RED= Formatting.RED
    val LIGHT_PURPLE= Formatting.LIGHT_PURPLE
    val YELLOW= Formatting.YELLOW
    val WHITE= Formatting.WHITE
    val OBFUSCATED= Formatting.OBFUSCATED
    val BOLD= Formatting.BOLD
    val STRIKETHROUGH= Formatting.STRIKETHROUGH
    val UNDERLINE= Formatting.UNDERLINE
    val ITALIC= Formatting.ITALIC
    val RESET= Formatting.RESET

    operator fun Formatting.unaryPlus(): StyleBuilder = this@StyleBuilder + this
    operator fun TextColor.unaryPlus(): StyleBuilder = this@StyleBuilder + this
    operator fun plus(format: Formatting): StyleBuilder = format(format)
    operator fun plus(color: TextColor): StyleBuilder = color(color)
    
    fun format(formatting: Formatting): StyleBuilder {
        style = style.withFormatting(formatting)
        return this
    }

    fun color(formatting: Formatting): StyleBuilder {
        style = style.withColor(formatting)
        return this
    }

    fun color(textColor: TextColor): StyleBuilder {
        style = style.withColor(textColor)
        return this
    }

    fun bold(): StyleBuilder {
        style = style.withBold(true)
        return this
    }

    fun bold(bold: Boolean): StyleBuilder {
        style = style.withBold(bold)
        return this
    }

    fun italic(): StyleBuilder {
        style = style.withItalic(true)
        return this
    }

    fun italic(italic: Boolean): StyleBuilder {
        style = style.withItalic(italic)
        return this
    }

    fun onHover(hoverEvent: HoverEvent): StyleBuilder {
        style = style.withHoverEvent(hoverEvent)
        return this
    }

    fun <T> onHover(action: HoverEvent.Action<T>, contents: T): StyleBuilder {
        style = style.withHoverEvent(HoverEvent(action, contents))
        return this
    }

    fun onClick(clickEvent: ClickEvent): StyleBuilder {
        style = style.withClickEvent(clickEvent)
        return this
    }

    fun onClick(action: ClickEvent.Action, value: String): StyleBuilder {
        style = style.withClickEvent(ClickEvent(action, value))
        return this
    }

    fun parent(parent: Style): StyleBuilder {
        style = style.withParent(parent)
        return this
    }
}

inline fun styleOf(f: StyleBuilder.() -> Unit): Style {
    val style = StyleBuilder(Style.EMPTY)
    style.f()
    return style.style
}
