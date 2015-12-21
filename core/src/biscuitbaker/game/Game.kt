package biscuitbaker.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import java.util.*

class Game {
    // TODO: Maybe make this a Long that is tenths of cookies owned?
    public var biscuits: Double = 0.0
        private set

    public var bps: Double = 0.0
        private set

    public var products: ArrayList<Product> = ArrayList()
        private set

    public var upgrades: ArrayList<Upgrade> = ArrayList()
        private set

    init {
        // Load products and upgrades
        var json = Json()

        val productsFile = Gdx.files.internal("data/products.json")
        val productJson = productsFile.readString()
        val productInfos = json.fromJson(ProductInfos::class.java, productJson)
        for (info in productInfos.products!!) {
            products.add(Product(info))
        }

        val upgradesFile = Gdx.files.internal("data/upgrades.json")
        val upgradeJson = upgradesFile.readString()
        val upgradeInfos = json.fromJson(UpgradeInfos::class.java, upgradeJson)
        for (info in upgradeInfos.upgrades!!) {
            upgrades.add(Upgrade(info))
        }
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

    fun buyProduct(productId: Int): Boolean {
        if (productId >= products.size) {
            return false
        }

        val product = products[productId]
        if (biscuits >= product.price) {
            biscuits -= product.price
            product.owned += 1
        }
        return true
    }

    fun buyUpgrade(upgradeId: Int): Boolean {
        if (upgradeId >= upgrades.size) {
            return false
        }

        val upgrade = upgrades[upgradeId]
        if (biscuits >= upgrade.price) {
            biscuits -= upgrade.price
            upgrade.purchased = true
        }
        return true
    }

    // TODO: implement
    //fun save() {
    //}
}