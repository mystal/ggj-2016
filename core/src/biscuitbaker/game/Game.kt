package biscuitbaker.game

import biscuitbaker.game.ui.Ui
import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import com.moandjiezana.toml.Toml
import java.util.*


class Game(val debug: Boolean) {
    var level: Int = 0
        private set

    var exp: Int = 0
        private set

    var expToLevel: ExpToLevel = ExpToLevel()
        private set

    val maxLevel: Int
        get() = expToLevel.numLevels

    val expToNextLevel: Int
        get() {
            if (level >= maxLevel) {
                return 0
            }
            return expToLevel.get(level) - exp
        }

    // TODO: Maybe make this a Long that is tenths of cookies owned?
    // TODO: Abstract away resources into a class so we can store those in data as well.

    // Biscuits
    var biscuits: Double = 0.0

    // Total number of biscuits ever earned
    var biscuitsEarned: Double = 0.0

    // Biscuits per second
    var bps: Double = 0.0
        private set

    // Biscuits per click
    val baseBpc: Double = 1.0

    var bpcBonus: Double = 0.0
    var bpcMultiplier: Double = 1.0

    // Biscuits per click
    val bpc: Double
        get() = (baseBpc + bpcBonus) * bpcMultiplier

    // Eclairs
    var eclairs: Double = 0.0

    // Total number of eclairs ever earned
    var eclairsEarned: Double = 0.0

    // Eclairs per second
    var eps: Double = 0.0
        private set

    // Cupcakes
    var cupcakes: Double = 0.0

    // Total number of cupcakes ever earned
    var cupcakesEarned: Double = 0.0

    // Cupcakes per second
    var cps: Double = 0.0
        private set

    // Pies
    var pies: Double = 0.0

    // Total number of pies ever earned
    var piesEarned: Double = 0.0

    // Pies per second
    var pps: Double = 0.0
        private set

    // TODO: Keep a HashMap of Products, Upgrades, and Events for quick lookup

    var products: ArrayList<Product> = ArrayList()
        private set

    var upgrades: ArrayList<Upgrade> = ArrayList()
        private set

    var eventManager: EventManager = EventManager()

    init {
        loadData()
    }

    fun update(dt: Float, ui: Ui) {
        // TODO: Save game state in the background.

        if (products.isEmpty()) {
            return;
        }

        // TODO: Don't update every frame. Update when buying an item or on some effect (or on a timer?)
        // Update biscuits per second
        bps = products.map {it.totalBps}.reduce {x, y -> x + y}
        // Update eclairs per second
        eps = products.map {it.totalEps}.reduce {x, y -> x + y}
        // Update cupcakes per second
        cps = products.map {it.totalCps}.reduce {x, y -> x + y}
        // Update pies per second
        pps = products.map {it.totalPps}.reduce {x, y -> x + y}

        // Earn those biscuits!
        earnBiscuits(bps * dt)
        earnEclairs(eps * dt)
        earnCupcakes(cps * dt)
        earnPies(pps * dt)

        eventManager.update(dt, this, ui)
    }

    fun addExp(amount: Int) {
        var remaining = amount
        // TODO: Don't allow negative values
        while (level < maxLevel && remaining > 0) {
            if (remaining < expToNextLevel) {
                exp += remaining
                remaining = 0
            } else {
                remaining -= expToNextLevel
                levelUp()
            }
        }
    }

    fun levelUp() {
        if (level < maxLevel) {
            exp = 0
            level += 1
        }
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

    fun earnEclairs(amount: Double) {
        eclairs += amount
        eclairsEarned += amount
    }

    fun spendEclairs(amount: Double) {
        eclairs -= amount
    }

    fun earnCupcakes(amount: Double) {
        cupcakes += amount
        cupcakesEarned += amount
    }

    fun spendCupcakes(amount: Double) {
        cupcakes -= amount
    }

    fun earnPies(amount: Double) {
        pies += amount
        piesEarned += amount
    }

    fun spendPies(amount: Double) {
        pies -= amount
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

    fun loadData() {
        // Load in text TOML files
        val productsToml = Toml().read(Gdx.files.internal("text/products.toml").readString());
        val upgradesToml = Toml().read(Gdx.files.internal("text/upgrades.toml").readString());

        // Load products and upgrades
        val json = Json()

        // TODO: Validate loaded data to ensure they meet certain requirements

        val expFile = Gdx.files.internal("data/exp.json")
        val expJson = expFile.readString()
        expToLevel = json.fromJson(ExpToLevel::class.java, expJson)

        val productsFile = Gdx.files.internal("data/products.json")
        val productJson = productsFile.readString()
        val productInfos = json.fromJson(ProductInfos::class.java, productJson)
        for (info in productInfos.products!!) {
            val product = Product(info)
            val table = productsToml.getTable("\"${product.name}\"")
            product.strings.flavor = table.getString("flavor")
            product.strings.description = table.getString("description")
            products.add(product)
        }

        val upgradesFile = Gdx.files.internal("data/upgrades.json")
        val upgradeJson = upgradesFile.readString()
        val upgradeInfos = json.fromJson(UpgradeInfos::class.java, upgradeJson)
        for (info in upgradeInfos.upgrades!!) {
            val upgrade = Upgrade(info)
            val table = upgradesToml.getTable("\"${upgrade.name}\"")
            upgrade.strings.flavor = table.getString("flavor")
            upgrade.strings.description = table.getString("description")
            upgrades.add(upgrade)
        }
    }

    fun saveState() {
        // Save game state.
        val saveData = GameSaveData(this)
        val json = Json()
        val saveDataString = json.prettyPrint(saveData)
        val saveDataFile = when (Gdx.app.type) {
            // On Android, put files in app private storage.
            ApplicationType.Android -> Gdx.files.local("profiles/profile1.json")
            // On Desktop, put files in home dir.
            ApplicationType.Desktop -> Gdx.files.external("profiles/profile1.json")
            else -> throw RuntimeException("Unsupported platform!")
        }
        saveDataFile.writeString(saveDataString, false)
    }

    // TODO: implement
    fun loadState() {
        // Ignore data that loaded from JSON
        // TODO: Load save file.
    }
}

class ExpToLevel {
    var expToLevel: ArrayList<Int> = ArrayList()
        private set

    val numLevels: Int
        get() = expToLevel.size

    fun get(level: Int): Int {
        return expToLevel[level]
    }
}