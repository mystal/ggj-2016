package biscuitbaker.game

import java.util.*

class Prerequisites() {
    public var biscuits: Long = 0

    // TODO: Support multiple products
    public var productCount: ProductCount? = null

    // TODO: Support multiple upgrades
    public var upgrade: String? = null

    public fun isSatisfied(game: Game): Boolean {
        // Check biscuit count
        if (game.biscuits < biscuits) {
            return false
        }

        // Check product count
        productCount?.let { productCount ->
            val product = game.products.find { product ->
                product.name == productCount.product
            }
            if (product != null) {
                if (product.owned < productCount.count) {
                    return false
                }
            } else {
                // TODO: Log error
            }
        }

        // Check upgrades
        upgrade?.let { upgradeName ->
            val upgrade = game.upgrades.find { upgrade ->
                upgrade.name == upgradeName
            }
            if (upgrade != null) {
                if (!upgrade.purchased) {
                    return false
                }
            } else {
                // TODO: Log error
            }
        }

        return true
    }
}

class ProductCount() {
    public var product: String = ""
    public var count: Int = 0
}
