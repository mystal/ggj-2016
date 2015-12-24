package biscuitbaker.game

import java.util.*

class UpgradeInfo() {
    public var name: String = ""
    public var price: Long = 0
    //public var effect: Effect
    public var requirements: ProductRequirements? = null
    public var effects: UpgradeEffects? = null
}

class ProductModifier() {
    public var product: String = ""
    public var value: Double = 0.0
}

class UpgradeEffects() {
    public var globalBpsBonus: Double = 0.0
    public var globalBpsMultiplier: Double = 0.0

    public var clickBonus: Double = 0.0
    public var clickMultiplier: Double = 0.0

    public var productBpsBonus: ProductModifier? = null
    public var productBpsMultiplier: ProductModifier? = null
}

class Upgrade(info: UpgradeInfo) {
    public var info: UpgradeInfo = info
        private set

    public var purchased: Boolean = false

    public val name: String
        get() = info.name

    public val price: Long
        get() = info.price

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

    public fun applyEffects(game: Game) {
        info.effects?.let { effects ->
            // TODO: Apply global BPS effects?

            game.bpcBonus += effects.clickBonus
            game.bpcMultiplier += effects.clickMultiplier

            effects.productBpsBonus?.let { productBpsBonus ->
                val product = game.products.find { product ->
                    product.name == productBpsBonus.product
                }
                if (product != null) {
                    product.bpsBonus += productBpsBonus.value
                } else {
                    // TODO: Log error
                }
            }

            effects.productBpsMultiplier?.let { productBpsMultiplier ->
                val product = game.products.find { product ->
                    product.name == productBpsMultiplier.product
                }
                if (product != null) {
                    product.bpsMultiplier += productBpsMultiplier.value
                } else {
                    // TODO: Log error
                }
            }
        }
    }
}

class UpgradeInfos() {
    public var upgrades: ArrayList<UpgradeInfo>? = null
}
