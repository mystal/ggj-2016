package biscuitbaker.game

// TODO: Rename to reflect use in Products and Upgrades
class ProductRequirements() {
    public var biscuits: Long = 0

    public var productCount: ProductCount? = null

    //var upgrades: ArrayList<String>

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

        return true
    }
}

class ProductCount() {
    public var product: String = ""
    public var count: Int = 0
}
