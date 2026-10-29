package com.mamamboga.app

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*

data class Produce(val name: String, val unit: String, val price: Int, val emoji: String, val category: String, val note: String)

class MainActivity : Activity() {
    private val green = Color.rgb(47, 107, 69)
    private val ink = Color.rgb(36, 57, 43)
    private val muted = Color.rgb(113, 128, 113)
    private val categories = listOf("All", "Vegetables", "Fruits", "Staples")
    private val produce = listOf(
        Produce("Sukuma wiki", "1 bunch", 30, "🥬", "Vegetables", "Fresh & leafy"),
        Produce("Tomatoes", "1 kg", 120, "🍅", "Vegetables", "Ripe and juicy"),
        Produce("Avocado", "1 piece", 40, "🥑", "Fruits", "Creamy goodness"),
        Produce("Red onions", "1 kg", 150, "🧅", "Vegetables", "Everyday essential"),
        Produce("Bananas", "1 bunch", 100, "🍌", "Fruits", "Naturally sweet"),
        Produce("Maize flour", "2 kg pack", 180, "🌽", "Staples", "A pantry favourite")
    )
    private val basket = linkedMapOf<Produce, Int>()
    private var selectedCategory = "All"
    private lateinit var productList: LinearLayout
    private lateinit var categoryRow: LinearLayout
    private lateinit var cartButton: TextView
    private lateinit var bottomCart: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        productList = findViewById(R.id.productList)
        categoryRow = findViewById(R.id.categoryRow)
        cartButton = findViewById(R.id.cartButton)
        bottomCart = findViewById(R.id.bottomCart)
        renderCategories()
        renderProducts()
        cartButton.setOnClickListener { showBasket() }
        bottomCart.setOnClickListener { showBasket() }
        findViewById<TextView>(R.id.whatsappBottom).setOnClickListener { openWhatsApp("Hi Mama Mboga! I'd like to ask about today's fresh produce.") }
        renderSocials()
    }

    private fun renderCategories() {
        categoryRow.removeAllViews()
        categories.forEach { category ->
            val chip = TextView(this).apply {
                text = category
                textSize = 13f
                setTypeface(null, Typeface.BOLD)
                setPadding(dp(17), dp(11), dp(17), dp(11))
                setTextColor(if (selectedCategory == category) Color.WHITE else ink)
                setBackgroundResource(if (selectedCategory == category) R.drawable.bg_green else R.drawable.bg_white_card)
                val params = LinearLayout.LayoutParams(-2, -2).apply { marginEnd = dp(8) }
                layoutParams = params
                setOnClickListener { selectedCategory = category; renderCategories(); renderProducts() }
            }
            categoryRow.addView(chip)
        }
    }

    private fun renderProducts() {
        productList.removeAllViews()
        produce.filter { selectedCategory == "All" || it.category == selectedCategory }.forEach { item ->
            val card = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp(13), dp(12), dp(12), dp(12))
                setBackgroundResource(R.drawable.bg_white_card)
                layoutParams = LinearLayout.LayoutParams(-1, -2).apply { bottomMargin = dp(9) }
            }
            val illustration = TextView(this).apply {
                text = item.emoji; textSize = 30f; gravity = Gravity.CENTER
                setBackgroundResource(R.drawable.bg_cream)
                layoutParams = LinearLayout.LayoutParams(dp(58), dp(58))
            }
            val detail = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(12), 0, dp(5), 0); layoutParams = LinearLayout.LayoutParams(0, -2, 1f) }
            detail.addView(label(item.name, 15f, ink, true))
            detail.addView(label("${item.unit}  ·  ${item.note}", 11f, muted, false).apply { setPadding(0, dp(4), 0, 0) })
            val right = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; gravity = Gravity.END or Gravity.CENTER_VERTICAL }
            right.addView(label("KSh ${item.price}", 14f, ink, true).apply { gravity = Gravity.END })
            val add = TextView(this).apply {
                text = if ((basket[item] ?: 0) > 0) "+  ${basket[item]}" else "+ Add"
                textSize = 12f; setTypeface(null, Typeface.BOLD); setTextColor(green)
                setPadding(dp(11), dp(7), dp(11), dp(7)); setBackgroundResource(R.drawable.bg_soft_green)
                layoutParams = LinearLayout.LayoutParams(-2, -2).apply { topMargin = dp(6) }
                setOnClickListener { basket[item] = (basket[item] ?: 0) + 1; updateBasket(); renderProducts() }
            }
            right.addView(add)
            card.addView(illustration); card.addView(detail); card.addView(right)
            productList.addView(card)
        }
    }

    private fun showBasket() {
        val items = basket.entries.toList()
        if (items.isEmpty()) {
            AlertDialog.Builder(this).setTitle("Your basket is waiting")
                .setMessage("Add a few fresh market picks to get started.")
                .setPositiveButton("Keep shopping", null).show()
            return
        }
        val content = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(dp(22), dp(6), dp(22), 0) }
        items.forEach { (item, qty) ->
            val row = TextView(this).apply {
                text = "${item.emoji}  ${item.name}  × $qty\n      KSh ${item.price * qty}"
                textSize = 14f; setTextColor(ink); setPadding(0, dp(8), 0, dp(8))
            }
            content.addView(row)
        }
        val total = items.sumOf { it.key.price * it.value }
        content.addView(TextView(this).apply { text = "Total  ·  KSh $total"; textSize = 17f; setTypeface(null, Typeface.BOLD); setTextColor(green); setPadding(0, dp(12), 0, dp(8)) })
        val modes = RadioGroup(this).apply { orientation = RadioGroup.VERTICAL }
        val delivery = RadioButton(this).apply { text = "Deliver to me"; id = View.generateViewId(); isChecked = true }
        val pickup = RadioButton(this).apply { text = "I'll pick it up"; id = View.generateViewId() }
        modes.addView(delivery); modes.addView(pickup)
        content.addView(TextView(this).apply { text = "How would you like it?"; textSize = 14f; setTypeface(null, Typeface.BOLD); setTextColor(ink); setPadding(0, dp(10), 0, 0) })
        content.addView(modes)
        content.addView(TextView(this).apply { text = "Payment: arrange M-PESA or cash with Mama Mboga on WhatsApp."; textSize = 12f; setTextColor(muted); setPadding(0, dp(6), 0, dp(4)) })
        AlertDialog.Builder(this).setTitle("Your basket")
            .setView(content)
            .setNegativeButton("Keep shopping", null)
            .setPositiveButton("Order on WhatsApp", null)
            .create().also { dialog ->
                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        val service = if (pickup.isChecked) "Pickup" else "Delivery"
                        val lines = items.joinToString("\n") { "• ${it.value} × ${it.key.name} — KSh ${it.key.price * it.value}" }
                        openWhatsApp("Hi Mama Mboga! I'd like to place an order ($service):\n$lines\nTotal: KSh $total\nPlease let me know the next step for payment.")
                        dialog.dismiss()
                    }
                }
                dialog.show()
            }
    }

    private fun renderSocials() {
        val row = findViewById<LinearLayout>(R.id.socialRow)
        // Replace these sample destinations with Mama Mboga's actual public profile URLs.
        val socials = listOf("Instagram" to "https://instagram.com/mamamboga", "Facebook" to "https://facebook.com/mamamboga", "X" to "https://x.com/mamamboga", "WhatsApp" to "https://wa.me/254700000000")
        socials.forEach { (name, url) ->
            val button = TextView(this).apply {
                text = name; textSize = 11f; setTypeface(null, Typeface.BOLD); setTextColor(green)
                gravity = Gravity.CENTER; setPadding(dp(9), dp(10), dp(9), dp(10)); setBackgroundResource(R.drawable.bg_soft_green)
                layoutParams = LinearLayout.LayoutParams(0, -2, 1f).apply { marginEnd = dp(5) }
                setOnClickListener { launchUrl(url) }
            }
            row.addView(button)
        }
    }

    private fun updateBasket() {
        val count = basket.values.sum()
        cartButton.text = "🛍  $count"
        bottomCart.text = "🛍  Basket${if (count > 0) " ($count)" else ""}"
    }

    private fun openWhatsApp(message: String) {
        val url = "https://wa.me/254700000000?text=${Uri.encode(message)}"
        launchUrl(url)
    }

    private fun launchUrl(url: String) {
        try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
        catch (_: Exception) { Toast.makeText(this, "No app available to open this link", Toast.LENGTH_SHORT).show() }
    }

    private fun label(text: String, size: Float, color: Int, bold: Boolean): TextView = TextView(this).apply {
        this.text = text; textSize = size; setTextColor(color); if (bold) setTypeface(null, Typeface.BOLD)
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}
