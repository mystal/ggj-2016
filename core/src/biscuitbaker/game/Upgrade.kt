package biscuitbaker.game

import java.util.*

class UpgradeInfo() {
    public var name: String = ""
    public var costs: Costs? = null
    //public var effect: Effect
    public var prereqs: Prerequisites? = null
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

    public var productEpsBonus: ProductModifier? = null
    public var productEpsMultiplier: ProductModifier? = null

    public var productCpsBonus: ProductModifier? = null
    public var productCpsMultiplier: ProductModifier? = null

    public var productPpsBonus: ProductModifier? = null
    public var productPpsMultiplier: ProductModifier? = null
}

class Upgrade(info: UpgradeInfo) {
    public var info: UpgradeInfo = info
        private set

    public var purchased: Boolean = false

    public val name: String
        get() = info.name

    // TODO: Fix prices to include other costs
    public val price: Long
        get() = info.costs?.biscuits ?: 0

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

    public fun applyEffects(game: Game) {
        info.effects?.let { effects ->
            // TODO: Apply global BPS effects?

            game.bpcBonus += effects.clickBonus
            game.bpcMultiplier += effects.clickMultiplier
            //Biscuit calculations
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

            //Eclair Calculations
            effects.productEpsBonus?.let { productEpsBonus ->
                val product = game.products.find { product ->
                    product.name == productEpsBonus.product
                }
                if (product != null) {
                    product.epsBonus += productEpsBonus.value
                } else {
                    // TODO: Log error
                }
            }

            effects.productEpsMultiplier?.let { productEpsMultiplier ->
                val product = game.products.find { product ->
                    product.name == productEpsMultiplier.product
                }
                if (product != null) {
                    product.epsMultiplier += productEpsMultiplier.value
                } else {
                    // TODO: Log error
                }
            }

            //Cupcake Calculations
            effects.productCpsBonus?.let { productCpsBonus ->
                val product = game.products.find { product ->
                    product.name == productCpsBonus.product
                }
                if (product != null) {
                    product.cpsBonus += productCpsBonus.value
                } else {
                    // TODO: Log error
                }
            }

            effects.productCpsMultiplier?.let { productCpsMultiplier ->
                val product = game.products.find { product ->
                    product.name == productCpsMultiplier.product
                }
                if (product != null) {
                    product.cpsMultiplier += productCpsMultiplier.value
                } else {
                    // TODO: Log error
                }
            }

            //Pie Calculations
            effects.productPpsBonus?.let { productPpsBonus ->
                val product = game.products.find { product ->
                    product.name == productPpsBonus.product
                }
                if (product != null) {
                    product.ppsBonus += productPpsBonus.value
                } else {
                    // TODO: Log error
                }
            }

            effects.productPpsMultiplier?.let { productPpsMultiplier ->
                val product = game.products.find { product ->
                    product.name == productPpsMultiplier.product
                }
                if (product != null) {
                    product.ppsMultiplier += productPpsMultiplier.value
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
