package biscuitbaker.game


class Prerequisites() {
    var biscuits: Long = 0
    var eclairs: Long = 0
    var cupcakes: Long = 0
    var pies: Long = 0

    // TODO: Support multiple products
    var productCount: ProductCount? = null

    // TODO: Support multiple upgrades
    var upgrade: String? = null

    var rank: Int = 0

    fun isSatisfied(game: Game): Boolean {
        // Check rank
        if (game.rank < rank){
            return false
        }

        // Check biscuit count
        if (game.biscuits < biscuits) {
            return false
        }

        // Check eclair count
        if (game.eclairs < eclairs) {
            return false
        }

        // Check cupcakes count
        if (game.cupcakes < cupcakes) {
            return false
        }

        // Check pie count
        if (game.pies < pies) {
            return false
        }
        // TODO: Check count for other resources

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

        // TODO: Check rank

        return true
    }
}

class ProductCount() {
    var product: String = ""
    var count: Int = 0
}
