package co.volight.soft.api.lang

import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import java.lang.StringBuilder

data class LangStr(val strs: List<LangStrType> = listOf()) {
    companion object {
        @JvmStatic
        fun parse(text: String) = co.volight.soft.impl.lang.parse(text)
    }

    fun toStr() = toStr(mapOf())
    fun toStr(args: Map<String, () -> String>): String {
        val strs = this.strs.map {
            when (it) {
                is LangStrType.Str -> it.str
                is LangStrType.Arg -> args[it.arg]?.let { it() } ?: "{{${it.arg}}}"
            }
        }
        val str = StringBuilder()
        for (text in strs) {
            str.append(text)
        }
        return str.toString()
    }
    inline fun toStr(args: Map<String, () -> String>.() -> Unit): String {
        val map = mutableMapOf<String, () -> String>()
        map.args()
        return  toStr(map)
    }

    fun toText() = toText(mapOf())
    fun toText(args: Map<String, () -> Text>): MutableText {
        val texts = this.strs.map {
            when (it) {
                is LangStrType.Str -> LiteralText(it.str)
                is LangStrType.Arg -> args[it.arg]?.let { it() } ?: LiteralText("{{${it.arg}}}")
            }
        }
        var root: MutableText = LiteralText("")
        for (text in texts) {
            root = root.append(text)
        }
        return root
    }
    inline fun toText(args: MutableMap<String, () -> Text>.() -> Unit): MutableText {
        val map = mutableMapOf<String, () -> Text>()
        map.args()
        return toText(map)
    }
}

sealed class LangStrType {
    data class Str(val str: String) : LangStrType()
    data class Arg(val arg: String) : LangStrType()
}
