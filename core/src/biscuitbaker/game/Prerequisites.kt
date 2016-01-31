package biscuitbaker.game

class Prerequisites() {
    public var biscuits: Long = 0
    public var eclairs: Long = 0
    public var cupcakes: Long = 0
    public var pies: Long = 0

    // TODO: Support multiple products
    public var productCount: ProductCount? = null

    // TODO: Support multiple upgrades
    public var upgrade: String? = null

    public var level: Int = 0

    public fun isSatisfied(game: Game): Boolean {
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

        // TODO: Check level

        return true
    }
}

class ProductCount() {
    public var product: String = ""
    public var count: Int = 0
}
