package co.volight.soft.impl.lang

import co.volight.soft.Soft
import co.volight.soft.api.lang.LangStr
import com.google.gson.Gson
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.PathMatcher

typealias TextName = String
typealias LangName = String
typealias ModName = String
typealias LangTextMap = Map<TextName, LangStr>
typealias LangMap = Map<LangName, LangTextMap>
typealias ModLangMap = MutableMap<ModName, LangMap>

object Lang {
    const val logName = "[SoftAPI:Lang]"
    val langs: ModLangMap = mutableMapOf()

    fun loadLangMap(modname: ModName, langDir: Path): LangMap {
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

    fun loadTextMap(path: Path): LangTextMap {
        val gson = Gson()
        val strMap = gson.fromJson<Map<String, String>>(Files.newBufferedReader(path), Map::class.java)
        return strMap.mapValues { LangStr.parse(it.value) }
    }
}
