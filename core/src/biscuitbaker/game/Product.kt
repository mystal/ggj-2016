package biscuitbaker.game

import java.util.*

// NOTE: Can't use a data class since JsonBeans expects a default constructor
class ProductInfo() {
    public var name: String = ""
    public var price: Long = 0
    public var bps: Double = 0.0
    public var eps: Double = 0.0
    public var cps: Double = 0.0
    public var pps: Double = 0.0
    public var prereqs: Prerequisites? = null
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

    // Biscuits
    public val baseBps: Double
        get() = info.bps

    public var bpsBonus: Double = 0.0
    public var bpsMultiplier: Double = 1.0

    public val bps: Double
        get() = (baseBps + bpsBonus) * bpsMultiplier

    public val totalBps: Double
        get() = bps * owned

    public var totalBiscuits: Double = 0.0

    // Eclairs
    public val baseEps: Double
        get() = info.eps

    public var epsBonus: Double = 0.0
    public var epsMultiplier: Double = 1.0

    public val eps: Double
        get() = (baseEps + epsBonus) * epsMultiplier

    public val totalEps: Double
        get() = eps * owned

    public var totalEclairs: Double = 0.0

    // Cupcakes
    public val baseCps: Double
        get() = info.cps

    public var cpsBonus: Double = 0.0
    public var cpsMultiplier: Double = 1.0

    public val cps: Double
        get() = (baseCps + cpsBonus) * cpsMultiplier

    public val totalCps: Double
        get() = cps * owned

    public var totalCupcakes: Double = 0.0

    // Pies
    public val basePps: Double
        get() = info.pps

    public var ppsBonus: Double = 0.0
    public var ppsMultiplier: Double = 1.0

    public val pps: Double
        get() = (basePps + ppsBonus) * ppsMultiplier

    public val totalPps: Double
        get() = pps * owned

    public var totalPies: Double = 0.0

    // Whether to display this product in the store. Once visible, it
    // will always be visible.
    private var visible: Boolean = false

    // TODO: Add several levels of visibility
    // e.g. show shadowed item first, then full item once it is affordable
    public fun isVisible(game: Game): Boolean {
        // Once visible, always visible
        if (!visible) {
            visible = info.prereqs?.isSatisfied(game) ?: true
        }
        return visible
    }
}

class ProductInfos() {
    public var products: ArrayList<ProductInfo>? = null
}