package com.shiraasulay.guitarchords

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.widget.EditText
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private val bg = Color.rgb(11, 16, 32)
    private val surface = Color.rgb(22, 29, 45)
    private val surface2 = Color.rgb(30, 39, 59)
    private val primary = Color.rgb(124, 92, 255)
    private val white = Color.rgb(248, 250, 252)
    private val muted = Color.rgb(148, 163, 184)

    private lateinit var listContainer: LinearLayout
    private lateinit var search: EditText
    private var selectedRoot: String? = null
    private var selectedQuality: String? = null
    private var favorites = linkedSetOf<String>()
    private val prefs by lazy { getSharedPreferences("guitar_chords", Context.MODE_PRIVATE) }
    private val chords by lazy { ChordLibrary.all() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = bg
        window.navigationBarColor = bg
        window.setSoftInputMode(0x10)
        loadFavorites()
        setContentView(buildUi())
        refreshCards()
    }

    private fun buildUi(): View {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(bg)
            layoutDirection = View.LAYOUT_DIRECTION_RTL
            textDirection = View.TEXT_DIRECTION_RTL
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 26, 24, 10)
        }

        header.addView(
            TextView(this).apply {
                text = "🎸  אקורדים לגיטרה"
                setTextColor(white)
                textSize = 28f
                typeface = Typeface.DEFAULT_BOLD
            },
            lp(-1, -2)
        )

        header.addView(
            TextView(this).apply {
                text = "למצוא את האקורד • לראות את האצבוע • להתחיל לנגן"
                setTextColor(muted)
                textSize = 14f
                setPadding(0, 5, 0, 0)
            },
            lp(-1, -2)
        )

        search = EditText(this).apply {
            hint = "חפש למשל C, Am, D7 או F♯maj7"
            setHintTextColor(Color.rgb(100, 116, 139))
            setTextColor(white)
            textSize = 16f
            setSingleLine(true)
            setPadding(18, 0, 18, 0)
            background = rounded(surface2, 22f)
        }

        header.addView(
            LinearLayout(this).apply {
                setPadding(0, 18, 0, 5)
                addView(search, lp(-1, 52))
            },
            lp(-1, -2)
        )
        root.addView(header, lp(-1, -2))

        root.addView(horizontalChips(buildRootChips()), lp(-1, 56))
        root.addView(horizontalChips(buildQualityChips()), lp(-1, 52))

        root.addView(
            TextView(this).apply {
                text = "○ פתוח   × לא מנגנים   •   המספרים בתוך העיגולים הם האצבעות"
                setTextColor(muted)
                textSize = 12f
                gravity = Gravity.CENTER
                setPadding(18, 0, 18, 2)
            },
            lp(-1, 34)
        )

        val scroll = ScrollView(this).apply { isFillViewport = true }
        listContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 5, 16, 30)
        }
        scroll.addView(listContainer, lp(-1, -2))
        root.addView(scroll, lp(-1, 0, 1f))

        search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { refreshCards() }
            override fun afterTextChanged(s: Editable?) = Unit
        })

        return root
    }

    private fun horizontalChips(content: LinearLayout): HorizontalScrollView =
        HorizontalScrollView(this).apply {
            isHorizontalScrollBarEnabled = false
            setPadding(16, 2, 16, 3)
            addView(content)
        }

    private fun buildRootChips(): LinearLayout {
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val all = TextView(this).apply {
            text = "הכול"
            setupChip()
            setOnClickListener {
                selectedRoot = null
                refreshCards()
                styleRootChips(row)
            }
        }
        row.addView(all, chipLp())

        ChordLibrary.roots.forEach { rootName ->
            val chip = TextView(this).apply {
                text = rootName
                setupChip()
                setOnClickListener {
                    selectedRoot = rootName
                    refreshCards()
                    styleRootChips(row)
                }
            }
            row.addView(chip, chipLp())
        }
        styleRootChips(row)
        return row
    }

    private fun buildQualityChips(): LinearLayout {
        val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val all = TextView(this).apply {
            text = "כל הסוגים"
            setupChip()
            setOnClickListener {
                selectedQuality = null
                refreshCards()
                styleQualityChips(row)
            }
        }
        row.addView(all, chipLp())

        ChordLibrary.qualities.forEach { pair ->
            val key = pair.first
            val label = pair.second
            val chip = TextView(this).apply {
                text = label
                tag = key
                setupChip()
                setOnClickListener {
                    selectedQuality = key
                    refreshCards()
                    styleQualityChips(row)
                }
            }
            row.addView(chip, chipLp())
        }
        styleQualityChips(row)
        return row
    }

    private fun TextView.setupChip() {
        textSize = 13f
        gravity = Gravity.CENTER
        setPadding(15, 0, 15, 0)
    }

    private fun styleRootChips(row: LinearLayout) {
        for (i in 0 until row.childCount) {
            val chip = row.getChildAt(i) as TextView
            val active = if (i == 0) selectedRoot == null else chip.text.toString() == selectedRoot
            chip.background = rounded(if (active) primary else surface2, 18f)
            chip.setTextColor(if (active) Color.WHITE else muted)
        }
    }

    private fun styleQualityChips(row: LinearLayout) {
        for (i in 0 until row.childCount) {
            val chip = row.getChildAt(i) as TextView
            val active = if (i == 0) selectedQuality == null else chip.tag == selectedQuality
            chip.background = rounded(if (active) primary else surface2, 18f)
            chip.setTextColor(if (active) Color.WHITE else muted)
        }
    }

    private fun refreshCards() {
        if (!::listContainer.isInitialized) return
        listContainer.removeAllViews()

        val q = search.text?.toString()?.trim()?.lowercase() ?: ""
        val filtered = chords.filter { chord ->
            val rootMatch = selectedRoot == null || chord.root == selectedRoot
            val qualityMatch = selectedQuality == null || chord.quality == selectedQuality
            val queryMatch = q.isBlank() ||
                chord.symbol.lowercase().contains(q) ||
                chord.root.lowercase().contains(q) ||
                ChordLibrary.qualityLabel(chord.quality).lowercase().contains(q)
            rootMatch && qualityMatch && queryMatch
        }

        listContainer.addView(
            TextView(this).apply {
                text = filtered.size.toString() + " אקורדים"
                setTextColor(muted)
                textSize = 13f
                setPadding(3, 7, 3, 3)
            },
            lp(-1, -2)
        )

        filtered.forEach { chord ->
            val card = makeChordCard(chord)
            val params = lp(-1, -2)
            params.topMargin = 12
            listContainer.addView(card, params)
        }

        if (filtered.isEmpty()) {
            listContainer.addView(
                TextView(this).apply {
                    text = "אין תוצאה. נסה C, Am, G7 או F♯."
                    setTextColor(white)
                    textSize = 16f
                    gravity = Gravity.CENTER
                    setPadding(20, 48, 20, 48)
                },
                lp(-1, 170)
            )
        }
    }

    private fun makeChordCard(chord: GuitarChord): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = rounded(surface, 22f)
            setPadding(18, 16, 18, 18)
            elevation = 3f
        }

        val top = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val labels = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        labels.addView(
            TextView(this).apply {
                text = chord.symbol
                setTextColor(white)
                textSize = 28f
                typeface = Typeface.DEFAULT_BOLD
            },
            lp(0, -2, 1f)
        )
        labels.addView(
            TextView(this).apply {
                text = ChordLibrary.qualityLabel(chord.quality) + "  •  " + chord.difficulty
                setTextColor(muted)
                textSize = 12f
                setPadding(0, 4, 0, 0)
            },
            lp(0, -2, 1f)
        )
        top.addView(labels, lp(0, -2, 1f))

        val fav = TextView(this).apply {
            text = if (favorites.contains(chord.symbol)) "★" else "☆"
            textSize = 28f
            gravity = Gravity.CENTER
            setTextColor(if (favorites.contains(chord.symbol)) Color.rgb(250, 204, 21) else muted)
            setOnClickListener {
                if (favorites.contains(chord.symbol)) favorites.remove(chord.symbol) else favorites.add(chord.symbol)
                prefs.edit().putStringSet("favorites", favorites.toSet()).apply()
                text = if (favorites.contains(chord.symbol)) "★" else "☆"
                setTextColor(if (favorites.contains(chord.symbol)) Color.rgb(250, 204, 21) else muted)
                Toast.makeText(
                    this@MainActivity,
                    if (favorites.contains(chord.symbol)) "נוסף למועדפים" else "הוסר מהמועדפים",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        top.addView(fav, lp(55, 50))
        card.addView(top, lp(-1, -2))

        val tabs = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(0, 8, 0, 1)
        }

        chord.shapes.forEachIndexed { index, ignored ->
            tabs.addView(
                TextView(this).apply {
                    text = "צורה " + (index + 1)
                    textSize = 11f
                    gravity = Gravity.CENTER
                    setPadding(12, 0, 12, 0)
                    setTextColor(if (index == 0) Color.WHITE else muted)
                    background = rounded(if (index == 0) primary else surface2, 15f)
                },
                chipLp()
            )
        }
        card.addView(tabs, lp(-1, 40))

        val diagram = ChordDiagramView(this).apply {
            setShape(chord.shapes.first())
            contentDescription = "תרשים אקורד " + chord.symbol
        }
        card.addView(diagram, lp(-1, 240))

        val desc = TextView(this).apply {
            text = chord.shapes.first().title
            setTextColor(muted)
            textSize = 12f
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 3)
        }
        card.addView(desc, lp(-1, -2))

        val fingers = TextView(this).apply {
            text = "E  A  D  G  B  e   |   " + chord.shapes.first().fingers.joinToString(" ")
            setTextColor(Color.rgb(203, 213, 225))
            textSize = 11f
            gravity = Gravity.CENTER
        }
        card.addView(fingers, lp(-1, -2))

        for (index in 0 until tabs.childCount) {
            (tabs.getChildAt(index) as TextView).setOnClickListener {
                val shape = chord.shapes[index]
                diagram.setShape(shape)
                desc.text = shape.title
                fingers.text = "E  A  D  G  B  e   |   " + shape.fingers.joinToString(" ")
                for (i in 0 until tabs.childCount) {
                    val t = tabs.getChildAt(i) as TextView
                    t.background = rounded(if (i == index) primary else surface2, 15f)
                    t.setTextColor(if (i == index) Color.WHITE else muted)
                }
            }
        }

        return card
    }

    private fun loadFavorites() {
        favorites = LinkedHashSet(prefs.getStringSet("favorites", emptySet()) ?: emptySet())
    }

    private fun rounded(color: Int, radius: Float) =
        GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
        }

    private fun lp(width: Int, height: Int, weight: Float = 0f) =
        LinearLayout.LayoutParams(width, height, weight)

    private fun chipLp(): LinearLayout.LayoutParams =
        LinearLayout.LayoutParams(-2, 40).apply {
            setMargins(4, 0, 4, 0)
        }
}
