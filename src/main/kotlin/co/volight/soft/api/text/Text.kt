package co.volight.soft.api.text

import co.volight.cell.Ref
import net.minecraft.text.MutableText
import net.minecraft.text.Style

inline fun MutableText.withStyle(f: Ref<Style>.() -> Unit) = this.setStyle(styleOf(f))