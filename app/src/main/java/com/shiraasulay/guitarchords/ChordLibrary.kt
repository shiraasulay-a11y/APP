package com.shiraasulay.guitarchords

data class ChordShape(
    val frets: IntArray,
    val fingers: IntArray,
    val title: String,
    val baseFret: Int = 1
)

data class GuitarChord(
    val root: String,
    val quality: String,
    val symbol: String,
    val shapes: List<ChordShape>,
    val difficulty: String
)

object ChordLibrary {
    val roots = listOf("C", "C♯", "D", "D♯", "E", "F", "F♯", "G", "G♯", "A", "A♯", "B")

    val qualities = listOf(
        "major" to "מז'ור",
        "minor" to "מינור",
        "7" to "7",
        "m7" to "m7",
        "maj7" to "Maj7",
        "sus2" to "sus2",
        "sus4" to "sus4",
        "6" to "6",
        "m6" to "m6",
        "add9" to "add9"
    )

    private val suffixes = mapOf(
        "major" to "",
        "minor" to "m",
        "7" to "7",
        "m7" to "m7",
        "maj7" to "maj7",
        "sus2" to "sus2",
        "sus4" to "sus4",
        "6" to "6",
        "m6" to "m6",
        "add9" to "add9"
    )

    private val eShapes = mapOf(
        "major" to ChordShape(intArrayOf(0,2,2,1,0,0), intArrayOf(1,3,4,2,1,1), "בארה • צורת E"),
        "minor" to ChordShape(intArrayOf(0,2,2,0,0,0), intArrayOf(1,3,4,1,1,1), "בארה • צורת Em"),
        "7" to ChordShape(intArrayOf(0,2,0,1,0,0), intArrayOf(1,3,1,2,1,1), "בארה • צורת E7"),
        "m7" to ChordShape(intArrayOf(0,2,0,0,0,0), intArrayOf(1,3,1,1,1,1), "בארה • צורת Em7"),
        "maj7" to ChordShape(intArrayOf(0,2,1,1,0,0), intArrayOf(1,3,2,4,1,1), "בארה • צורת Emaj7"),
        "sus2" to ChordShape(intArrayOf(0,2,2,4,0,0), intArrayOf(1,3,4,4,1,1), "בארה • צורת Esus2"),
        "sus4" to ChordShape(intArrayOf(0,2,2,2,0,0), intArrayOf(1,3,4,2,1,1), "בארה • צורת Esus4"),
        "6" to ChordShape(intArrayOf(0,2,2,1,2,0), intArrayOf(1,3,4,2,3,1), "בארה • צורת E6"),
        "m6" to ChordShape(intArrayOf(0,2,2,0,2,0), intArrayOf(1,3,4,1,3,1), "בארה • צורת Em6"),
        "add9" to ChordShape(intArrayOf(0,2,4,1,0,0), intArrayOf(1,3,4,2,1,1), "בארה • צורת Eadd9")
    )

    private val aShapes = mapOf(
        "major" to ChordShape(intArrayOf(-1,0,2,2,2,0), intArrayOf(0,1,3,3,3,1), "בארה • צורת A"),
        "minor" to ChordShape(intArrayOf(-1,0,2,2,1,0), intArrayOf(0,1,3,4,2,1), "בארה • צורת Am"),
        "7" to ChordShape(intArrayOf(-1,0,2,0,2,0), intArrayOf(0,1,3,1,4,1), "בארה • צורת A7"),
        "m7" to ChordShape(intArrayOf(-1,0,2,0,1,0), intArrayOf(0,1,3,1,2,1), "בארה • צורת Am7"),
        "maj7" to ChordShape(intArrayOf(-1,0,2,1,2,0), intArrayOf(0,1,3,2,4,1), "בארה • צורת Amaj7"),
        "sus2" to ChordShape(intArrayOf(-1,0,2,2,0,0), intArrayOf(0,1,3,3,1,1), "בארה • צורת Asus2"),
        "sus4" to ChordShape(intArrayOf(-1,0,2,2,3,0), intArrayOf(0,1,2,3,4,1), "בארה • צורת Asus4"),
        "6" to ChordShape(intArrayOf(-1,0,2,2,2,2), intArrayOf(0,1,3,3,3,4), "בארה • צורת A6"),
        "m6" to ChordShape(intArrayOf(-1,0,2,2,1,2), intArrayOf(0,1,3,3,2,4), "בארה • צורת Am6"),
        "add9" to ChordShape(intArrayOf(-1,0,2,2,0,0), intArrayOf(0,1,3,3,1,1), "בארה • צורת Aadd9")
    )

    private val openShapes = mapOf(
        "C|major" to ChordShape(intArrayOf(-1,3,2,0,1,0), intArrayOf(0,3,2,0,1,0), "פתוח • C"),
        "D|major" to ChordShape(intArrayOf(-1,-1,0,2,3,2), intArrayOf(0,0,0,1,3,2), "פתוח • D"),
        "E|major" to ChordShape(intArrayOf(0,2,2,1,0,0), intArrayOf(0,2,3,1,0,0), "פתוח • E"),
        "G|major" to ChordShape(intArrayOf(3,2,0,0,0,3), intArrayOf(2,1,0,0,0,3), "פתוח • G"),
        "A|major" to ChordShape(intArrayOf(-1,0,2,2,2,0), intArrayOf(0,0,1,2,3,0), "פתוח • A"),
        "F|major" to ChordShape(intArrayOf(1,3,3,2,1,1), intArrayOf(1,3,4,2,1,1), "בארה • F"),
        "A|minor" to ChordShape(intArrayOf(-1,0,2,2,1,0), intArrayOf(0,0,2,3,1,0), "פתוח • Am"),
        "D|minor" to ChordShape(intArrayOf(-1,-1,0,2,3,1), intArrayOf(0,0,0,2,3,1), "פתוח • Dm"),
        "E|minor" to ChordShape(intArrayOf(0,2,2,0,0,0), intArrayOf(0,2,3,0,0,0), "פתוח • Em"),
        "A|7" to ChordShape(intArrayOf(-1,0,2,0,2,0), intArrayOf(0,0,2,0,3,0), "פתוח • A7"),
        "D|7" to ChordShape(intArrayOf(-1,-1,0,2,1,2), intArrayOf(0,0,0,2,1,3), "פתוח • D7"),
        "E|7" to ChordShape(intArrayOf(0,2,0,1,0,0), intArrayOf(0,2,0,1,0,0), "פתוח • E7")
    )

    fun all(): List<GuitarChord> = buildList {
        roots.forEachIndexed { index, root ->
            qualities.forEach { pair ->
                val quality = pair.first
                val symbol = root + suffixes.getValue(quality)
                val shapes = mutableListOf<ChordShape>()

                openShapes["$root|$quality"]?.let { shapes.add(it) }

                val eFret = eShapeRootFret(index)
                val aFret = aShapeRootFret(index)

                if (eFret in 1..12) shapes.add(shift(eShapes.getValue(quality), eFret))
                if (aFret in 1..12) shapes.add(shift(aShapes.getValue(quality), aFret))

                if (shapes.isEmpty()) shapes.add(shift(eShapes.getValue(quality), 1))

                val difficulty = when {
                    shapes.any { it.title.startsWith("פתוח") } -> "קל"
                    shapes.any { it.baseFret <= 3 } -> "בינוני"
                    else -> "מתקדם"
                }

                add(GuitarChord(root, quality, symbol, shapes.distinctBy { it.frets.toList() }, difficulty))
            }
        }
    }

    private fun eShapeRootFret(rootIndex: Int): Int {
        val value = (rootIndex - 4 + 12) % 12
        return if (value == 0) 12 else value
    }

    private fun aShapeRootFret(rootIndex: Int): Int {
        val value = (rootIndex - 9 + 12) % 12
        return if (value == 0) 12 else value
    }

    private fun shift(shape: ChordShape, rootFret: Int): ChordShape {
        val shifted = shape.frets.map { fret ->
            when {
                fret < 0 -> -1
                fret == 0 -> rootFret
                else -> rootFret + fret
            }
        }.toIntArray()
        return shape.copy(frets = shifted, baseFret = rootFret)
    }

    fun qualityLabel(quality: String): String =
        qualities.firstOrNull { it.first == quality }?.second ?: quality
}
