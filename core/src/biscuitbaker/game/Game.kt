package biscuitbaker.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import java.util.*

class Game(val debug: Boolean) {
    // TODO: Maybe make this a Long that is tenths of cookies owned?
    // Number of biscuits
    public var biscuits: Double = 0.0

    // Total number of biscuits ever earned
    public var biscuitsEarned: Double = 0.0

    // Biscuits per second
    public var bps: Double = 0.0
        private set

    // Biscuits per click
    public val baseBpc: Double = 1.0

    public var bpcBonus: Double = 0.0
    public var bpcMultiplier: Double = 1.0

    // Biscuits per click
    public val bpc: Double
        get() = (baseBpc + bpcBonus) * bpcMultiplier

    // TODO: Keep a HashMap of Proucts and Upgrades for quick lookup

    public var products: ArrayList<Product> = ArrayList()
        private set

    public var upgrades: ArrayList<Upgrade> = ArrayList()
        private set

    init {
        loadStock()
        loadState()
    }

    fun update(dt: Float) {
        if (products.isEmpty()) {
            return;
        }

        // TODO: Don't update every frame. Update when buying an item or on some effect (or on a timer?)
        // Update biscuits per second
        bps = products.map {it.totalBps}.reduce {x, y -> x + y}

        // Earn those biscuits!
        earnBiscuits(bps * dt)
    }

    fun click() {
        earnBiscuits(bpc)
    }

    fun earnBiscuits(amount: Double) {
        biscuits += amount
        biscuitsEarned += amount
    }

    fun spendBiscuits(amount: Double) {
        biscuits -= amount
    }

    fun buyProduct(productId: Int): Boolean {
        if (productId >= products.size) {
            return false
        }

        val product = products[productId]
        if (biscuits >= product.price) {
            spendBiscuits(product.price.toDouble())
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
            spendBiscuits(upgrade.price.toDouble())
            upgrade.purchased = true

            // Apply upgrade's effects
            upgrade.applyEffects(this)
        }
        return true
    }

    fun loadStock() {
        // Load products and upgrades
        val json = Json()

        // TODO: Validate loaded data to ensure they meet certain requirements

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

    // TODO: implement
    fun saveState() {
    }

    // TODO: implement
    fun loadState() {
        // TODO: Save state of products and upgrades
        // Ignore data that loaded from JSON
    }
}