package biscuitbaker.game

import java.util.*

// NOTE: Can't use a data class since JsonBeans expects a default constructor
class ProductInfo() {
    var name: String = ""
    var price: Long = 0
    var bps: Double = 0.0
    var eps: Double = 0.0
    var cps: Double = 0.0
    var pps: Double = 0.0
    var prereqs: Prerequisites? = null
}

class ProductText() {
    var flavor: String = ""
}

class Product(info: ProductInfo) {
    var info: ProductInfo = info
        private set

    var strings: FlavorStrings = FlavorStrings()
        private set

    var owned: Int = 0

    val name: String
        get() = info.name

    // TODO: Make this increase with owned amount
    val price: Long
        get() = info.price

    // Biscuits
    val baseBps: Double
        get() = info.bps

    var bpsBonus: Double = 0.0
    var bpsMultiplier: Double = 1.0

    val bps: Double
        get() = (baseBps + bpsBonus) * bpsMultiplier

    val totalBps: Double
        get() = bps * owned

    var totalBiscuits: Double = 0.0

    // Eclairs
    val baseEps: Double
        get() = info.eps

    var epsBonus: Double = 0.0
    var epsMultiplier: Double = 1.0

    val eps: Double
        get() = (baseEps + epsBonus) * epsMultiplier

    val totalEps: Double
        get() = eps * owned

    var totalEclairs: Double = 0.0

    // Cupcakes
    val baseCps: Double
        get() = info.cps

    var cpsBonus: Double = 0.0
    var cpsMultiplier: Double = 1.0

    val cps: Double
        get() = (baseCps + cpsBonus) * cpsMultiplier

    val totalCps: Double
        get() = cps * owned

    var totalCupcakes: Double = 0.0

    // Pies
    val basePps: Double
        get() = info.pps

    var ppsBonus: Double = 0.0
    var ppsMultiplier: Double = 1.0

    val pps: Double
        get() = (basePps + ppsBonus) * ppsMultiplier

    val totalPps: Double
        get() = pps * owned

    var totalPies: Double = 0.0

    // Whether to display this product in the store. Once visible, it
    // will always be visible.
    private var visible: Boolean = false

    // TODO: Add several levels of visibility
    // e.g. show shadowed item first, then full item once it is affordable
    fun isVisible(game: Game): Boolean {
        // Once visible, always visible
        if (!visible) {
            visible = info.prereqs?.isSatisfied(game) ?: true
        }
        return visible
    }
}

class ProductInfos() {
    var products: ArrayList<ProductInfo>? = null
}