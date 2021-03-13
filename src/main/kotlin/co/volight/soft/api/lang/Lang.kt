package co.volight.soft.api.lang

import co.volight.cell.Out
import co.volight.soft.Soft
import co.volight.soft.api.text.StyleBuilder
import co.volight.soft.api.text.style
import co.volight.soft.api.text.toText
import co.volight.soft.impl.events.PlayerLangInfo
import co.volight.soft.impl.lang.*
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.*
import kotlin.concurrent.fixedRateTimer

object Lang {
    fun get(modName: ModName): LangHandle? {
        return LangImpl.get(modName)
    }

    fun reg(modName: ModName, path: String? = null): LangHandle? {
        return LangImpl.reg(modName, path)
    }
}

data class LangHandle(val modname: ModName, private val map: LangMap) {
    fun getPlayerLang(player: ServerPlayerEntity): LangName {
        return PlayerLangInfo.get(player)
    }

    fun getStr(langName: LangName, textName: TextName): LangStr? {
        return map[langName]?.get(textName)
    }
    fun getStr(player: ServerPlayerEntity, textName: TextName): LangStr? {
        val langName = getPlayerLang(player)
        return getStr(langName, textName)
    }
    fun getStr(player: ServerPlayerEntity, textName: TextName, langName: Out<LangName>): LangStr? {
        langName *= getPlayerLang(player)
        return getStr(!langName, textName)
    }

    fun getBuilder(langName: LangName): LangTextBuild? {
        val langmap = map[langName]
        if (langmap == null) {
            Soft.LOGGER.info("$logName The language \"${langName}\" does not exist")
            return null
        }
        return LangTextBuild(langmap)
    }


    fun langText(langName: LangName, textName: TextName) = langText(langName, textName, mapOf())
    fun langText(langName: LangName, textName: TextName, args: Map<String, () -> Text>) : MutableText {
        val str = getStr(langName, textName)
        if (str == null) {
            Soft.LOGGER.info("$logName The language \"${langName}\" does not exist")
            return LiteralText(textName)
        }
        return str.toText(args)
    }
    inline fun langText(langName: LangName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit) : MutableText {
        val map = mutableMapOf<String, () -> Text>()
        map.args()
        return langText(langName, textName, map)
    }
    inline fun langText(langName: LangName, f: LangTextBuild.() -> Unit) : MutableText {
        val build = getBuilder(langName) ?: return LiteralText("$logName The language \"${langName}\" does not exist").style { +RED }
        build.f()
        return build.text
    }


    fun langText(player: ServerPlayerEntity, textName: TextName) = langText(player, textName, mapOf())
    fun langText(player: ServerPlayerEntity, textName: TextName, args: Map<String, () -> Text>): MutableText {
        val langName = Out<LangName>()
        val str = getStr(player, textName, langName)
        if (str == null) {
            Soft.LOGGER.info("$logName The language \"${!langName}\" used by player ${player.entityName} does not exist")
            return LiteralText(textName)
        }
        return str.toText(args)
    }
    inline fun langText(player: ServerPlayerEntity, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit): MutableText {
        val map = mutableMapOf<String, () -> Text>()
        map.args()
        return langText(player, textName, map)
    }
    inline fun langText(player: ServerPlayerEntity, f: LangTextBuild.() -> Unit) : MutableText {
        val langName = getPlayerLang(player)
        val build = getBuilder(langName) ?: return LiteralText("$logName The language \"${langName}\" does not exist").style { +RED }
        build.f()
        return build.text
    }
}

class LangTextBuild(val map: LangTextMap) {
    var text: MutableText = LiteralText("")

    operator fun Text.unaryPlus() = this@LangTextBuild + this
    operator fun plus(text: Text): LangTextBuild {
        this.text.append(text)
        return this
    }
    operator fun String.unaryPlus() = this@LangTextBuild + this
    operator fun plus(str: String): LangTextBuild {
        this.text.append(LiteralText(str))
        return this
    }

    fun literal(str: String) = str.toText()
    fun selector(str: String) = SelectorText(str)
    fun keybind(str: String) = KeybindText(str)

    fun lang(textName: String): MutableText {
        return map[textName]?.toText() ?: textName.toText()
    }
    fun lang(textName: String, args: Map<String, () -> Text>): MutableText {
        return map[textName]?.toText(args) ?: textName.toText()
    }
    inline fun lang(textName: String, args: MutableMap<String, () -> Text>.() -> Unit): MutableText {
        return map[textName]?.toText(args) ?: textName.toText()
    }
    inline fun lang(textName: String, style: StyleBuilder.() -> Unit, args: MutableMap<String, () -> Text>.() -> Unit): MutableText {
        return map[textName]?.toText(args)?.style(style) ?: textName.toText()
    }
}
