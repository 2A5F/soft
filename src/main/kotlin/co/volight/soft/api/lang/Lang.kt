package co.volight.soft.api.lang

import co.volight.cell.Out
import co.volight.soft.Soft
import co.volight.soft.impl.events.PlayerLangInfo
import co.volight.soft.impl.lang.Lang.langs
import co.volight.soft.impl.lang.Lang.loadLangMap
import co.volight.soft.impl.lang.Lang.logName
import co.volight.soft.impl.lang.LangMap
import co.volight.soft.impl.lang.LangName
import co.volight.soft.impl.lang.ModName
import co.volight.soft.impl.lang.TextName
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text

object Lang {
    fun getStr(modName: ModName, langName: LangName, textName: TextName): LangStr? {
        return langs[modName]?.get(langName)?.get(textName)
    }
    fun getStr(modName: ModName, player: ServerPlayerEntity, textName: TextName): LangStr? {
        val langName = PlayerLangInfo.get(player)
        return getStr(modName, langName, textName)
    }
    fun getStr(modName: ModName, player: ServerPlayerEntity, textName: TextName, langName: Out<LangName>): LangStr? {
        langName *= PlayerLangInfo.get(player)
        return getStr(modName, !langName, textName)
    }

    fun langText(modName: ModName, langName: LangName, textName: TextName) : MutableText = langText(langName, modName, textName, mapOf())

    fun langText(modName: ModName, langName: LangName, textName: TextName, args: Map<String, () -> Text>) : MutableText {
        val str = getStr(modName, langName, textName)
        if (str == null) {
            Soft.LOGGER.info("$logName The language \"${langName}\" does not exist")
            return LiteralText(textName)
        }
        return str.toText(args)
    }

    fun langText(modName: ModName, langName: LangName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit) : MutableText {
        val map = mutableMapOf<String, () -> Text>()
        map.args()
        return langText(langName, modName, textName, map)
    }

    fun langText(modName: ModName, player: ServerPlayerEntity, textName: TextName): MutableText = langText(modName, player, textName, mapOf())

    fun langText(modName: ModName, player: ServerPlayerEntity, textName: TextName, args: Map<String, () -> Text>): MutableText {
        val langName = Out<LangName>()
        val str = Lang.getStr(modName, player, textName, langName)
        if (str == null) {
            Soft.LOGGER.info("$logName The language \"${!langName}\" used by player ${player.entityName} does not exist")
            return LiteralText(textName)
        }
        return str.toText(args)
    }

    inline fun langText(modName: ModName, player: ServerPlayerEntity, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit): MutableText {
        val map = mutableMapOf<String, () -> Text>()
        map.args()
        return langText(modName, player, textName, map)
    }

    fun getLang(modeName: ModName): LangHandle? {
        return langs[modeName]?.let { LangHandle(modeName, it) }
    }

    fun reg(modeName: ModName, path: String? = null): LangHandle? {
        val container = FabricLoader.getInstance().getModContainer(modeName).orElseThrow { throw RuntimeException("Mod \"${modeName}\" not loaded") }
        val langDir = container.getPath(path ?: "assets/${modeName}/lang/")
        return try {
            val langMap = loadLangMap(modeName, langDir)
            langs[modeName] = langMap
            Soft.LOGGER.info("$logName Language files of mod \"${modeName}\" loaded")
            LangHandle(modeName, langMap)
        } catch (e: Exception) {
            Soft.LOGGER.error("$logName Failed to load the language file of mod \"${modeName}\"", e)
            null
        }
    }
}

data class LangHandle(val modname: ModName, val map: LangMap) {
    fun getStr(langName: LangName, textName: TextName): LangStr? {
        return map.get(langName)?.get(textName)
    }
    fun getStr(player: ServerPlayerEntity, textName: TextName): LangStr? {
        val langName = PlayerLangInfo.get(player)
        return getStr(langName, textName)
    }
    fun getStr(player: ServerPlayerEntity, textName: TextName, langName: Out<LangName>): LangStr? {
        langName *= PlayerLangInfo.get(player)
        return getStr(!langName, textName)
    }

    fun langText(langName: LangName, textName: TextName) : MutableText = langText(langName, textName, mapOf())

    fun langText(langName: LangName, textName: TextName, args: Map<String, () -> Text>) : MutableText {
        val str = getStr(langName, textName)
        if (str == null) {
            Soft.LOGGER.info("$logName The language \"${langName}\" does not exist")
            return LiteralText(textName)
        }
        return str.toText(args)
    }

    fun langText(langName: LangName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit) : MutableText {
        val map = mutableMapOf<String, () -> Text>()
        map.args()
        return langText(langName, textName, map)
    }

    fun langText(player: ServerPlayerEntity, textName: TextName): MutableText = langText(player, textName, mapOf())

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
}

fun ServerPlayerEntity.langText(modName: ModName, textName: TextName): MutableText = Lang.langText(modName, this, textName)

fun ServerPlayerEntity.langText(modName: ModName, textName: TextName, args: Map<String, () -> Text>): MutableText {
    return Lang.langText(modName, this, textName, args)
}

inline fun ServerPlayerEntity.langText(modName: ModName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit): MutableText {
    return Lang.langText(modName, this, textName, args)
}

fun langText(modName: ModName, langName: LangName, textName: TextName) : MutableText = Lang.langText(modName, langName, textName)

fun langText(modName: ModName, langName: LangName, textName: TextName, args: Map<String, () -> Text>) : MutableText {
    return Lang.langText(modName, langName, textName, args)
}

fun langText(modName: ModName, langName: LangName, textName: TextName, args: MutableMap<String, () -> Text>.() -> Unit) : MutableText {
    return Lang.langText(modName, langName, textName, args)
}
