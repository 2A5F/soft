package co.volight.soft.impl.lang

import co.volight.soft.api.lang.LangStr
import co.volight.soft.api.lang.LangStrType
import co.volight.utils.subLast
import co.volight.utils.tryGet

fun parse(text: String): LangStr {
    val chars = text.toCharArray().toList()
    val texts = sequence { pRoot(chars) }.toList()
    return LangStr(texts)
}

tailrec suspend fun SequenceScope<LangStrType>.pRoot(code: List<Char>, index: Int = 0) {
    when (code.tryGet(index)) {
        '{' -> {
            val (last, arg) = pParam(code.subLast(index)) ?: return this.pRoot(code, index + 1)
            val sub = code.subList(0, index)
            if (sub.isNotEmpty()) {
                val str = sub.toCharArray().concatToString()
                yield(LangStrType.Str(str))
            }
            yield(arg)
            return this.pRoot(last)
        }
        null -> {
            val str = code.subList(0, index).toCharArray().concatToString()
            yield(LangStrType.Str(str))
            return
        }
        else -> return this.pRoot(code, index + 1)
    }
}

fun pParam(code: List<Char>): Pair<List<Char>, LangStrType.Arg>? = when (code.tryGet(0)) {
    '{' -> when (code.tryGet(1)) {
        '{' -> when (code.tryGet(2)) {
            null, '{', '}' -> null
            else -> pParamBody(code, 3)
        }
        else -> null
    }
    else -> null
}

tailrec fun pParamBody(code: List<Char>, index: Int): Pair<List<Char>, LangStrType.Arg>? = when (code.tryGet(index)) {
    null -> null
    '}' -> when(code.tryGet(index + 1)) {
        '}' -> {
            val str = code.subList(2, index).toCharArray().concatToString()
            Pair(code.subLast(index + 2), LangStrType.Arg(str))
        }
        else -> null
    }
    else -> pParamBody(code, index + 1)
}