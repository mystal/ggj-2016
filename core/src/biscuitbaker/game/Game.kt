package biscuitbaker.game

import java.util.*

class Game {
    // TODO: Make this a Long that is tenths of cookies owned
    public var biscuits: Double = 0.0
        private set

    public var products: ArrayList<Product> = ArrayList()
        private set

    public var bps: Double = 0.0
        private set

    init {
        // TODO: Dynamically load products
        products.add(Product("Cursor", 10, 0.1))
    }

    fun update(dt: Float) {
        if (products.isEmpty()) {
            return;
        }
        bps = products.map {it.bps * it.owned}.reduce {x, y -> x + y}
        biscuits += bps * dt
    }

    fun click() {
        biscuits += 1
    }

    fun buy(productId: Int) {
        if (productId >= products.size) {
            return
        }
        val product = products[productId]
        if (biscuits >= product.price) {
            biscuits -= product.price
            product.owned += 1
        }
    }
}