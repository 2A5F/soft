package co.volight.soft.impl.lang

import co.volight.soft.ModId
import co.volight.soft.Soft
import co.volight.soft.api.lang.LangHandle
import co.volight.soft.api.lang.LangStr
import com.google.gson.Gson
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.PathMatcher

typealias TextName = String
typealias LangName = String
typealias LangTextMap = Map<TextName, LangStr>
typealias LangMap = Map<LangName, LangTextMap>
typealias ModLangMap = MutableMap<ModId, LangHandle>

internal const val logName = "[SoftAPI:Lang]"

internal object LangImpl {
    private val langs: ModLangMap = mutableMapOf()

    fun get(modeName: ModId): LangHandle? {
        return langs[modeName]
    }

    fun reg(modeName: ModId, path: String? = null): LangHandle? {
        val container = FabricLoader.getInstance().getModContainer(modeName).orElseThrow { throw RuntimeException("Mod \"${modeName}\" not loaded") }
        val langDir = container.getPath(path ?: "assets/${modeName}/lang/")
        return try {
            val langMap = loadLangMap(modeName, langDir)
            val handle = LangHandle(modeName, langMap)
            langs[modeName] = handle
            Soft.LOGGER.info("$logName Language files of mod \"${modeName}\" loaded")
            handle
        } catch (e: Exception) {
            Soft.LOGGER.error("$logName Failed to load the language file of mod \"${modeName}\"", e)
            null
        }
    }

    private fun loadLangMap(modname: ModId, langDir: Path): LangMap {
        val jsonFile: PathMatcher = langDir.fileSystem.getPathMatcher("glob:**/*.json")
        return sequence {
            Files.walk(langDir).use { paths ->
                for (path in paths.filter { !Files.isDirectory(it) && jsonFile.matches(it) }) {
                    Soft.LOGGER.debug("$logName loading $path")
                    val textMap = loadTextMap(path)
                    val rawMame = path.fileName.toString()
                    val name = rawMame.substring(0, rawMame.length - 5)
                    Soft.LOGGER.info("$logName mod \"${modname}\" loaded lang \"${name}\"")
                    yield(Pair(name, textMap))
                }
            }
        }.toMap()
    }

    private fun loadTextMap(path: Path): LangTextMap {
        val gson = Gson()
        val strMap = gson.fromJson<Map<String, String>>(Files.newBufferedReader(path), Map::class.java)
        return strMap.mapValues { LangStr.parse(it.value) }
    }
}
