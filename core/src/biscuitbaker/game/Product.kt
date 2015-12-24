package biscuitbaker.game

import java.util.*

// NOTE: Can't use a data class since JsonBeans expects a default constructor
class ProductInfo() {
    public var name: String = ""
    public var price: Long = 0
    public var bps: Double = 0.0
    public var requirements: ProductRequirements? = null
}

class Product(info: ProductInfo) {
    public var info: ProductInfo = info
        private set

    public var owned: Int = 0

    public val name: String
        get() = info.name

    // TODO: Make this increase with owned amount
    public val price: Long
        get() = info.price

    public val baseBps: Double
        get() = info.bps

    public var bpsBonus: Double = 0.0
    public var bpsMultiplier: Double = 1.0

    public val bps: Double
        get() = (baseBps + bpsBonus) * bpsMultiplier

    public val totalBps: Double
        get() = bps * owned

    public var totalBiscuits: Double = 0.0

    // Whether to display this product in the store. Once visible, it
    // will always be visible.
    private var visible: Boolean = false

    // TODO: Add several levels of visibility
    // e.g. show shadowed item first, then full item once it is affordable
    public fun isVisible(game: Game): Boolean {
        // Once visible, always visible
        if (!visible) {
            visible = info.requirements?.isSatisfied(game) ?: true
        }
        return visible
    }
}

class ProductInfos() {
    public var products: ArrayList<ProductInfo>? = null
}