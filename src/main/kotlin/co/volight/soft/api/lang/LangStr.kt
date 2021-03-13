package co.volight.soft.api.lang

import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import java.lang.RuntimeException

data class LangStr(val strs: List<LangStrType> = listOf()) {
    companion object {
        @JvmStatic
        fun parse(text: String) = co.volight.soft.impl.lang.parse(text)
    }

    fun toText(): MutableText {
        return toText(mapOf())
    }
    fun toText(args: Map<String, () -> Text>): MutableText {
        val texts = this.strs.map {
            when (it) {
                is LangStrType.Str -> LiteralText(it.str)
                is LangStrType.Arg -> args[it.arg]?.let { it() } ?: LiteralText("{{${it.arg}}}")
                else -> throw RuntimeException("Never Branch")
            }
        }
        var root: MutableText = LiteralText("")
        for (text in texts) {
            root = root.append(text)
        }
        return root
    }
}

interface LangStrType {
    data class Str(val str: String) : LangStrType
    data class Arg(val arg: String) : LangStrType
}
