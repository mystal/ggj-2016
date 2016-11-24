package biscuitbaker.game

import biscuitbaker.game.ui.Ui
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import com.moandjiezana.toml.Toml
import java.util.*


class Game(val debug: Boolean) {
    var rank: Int = 0
        private set

    var exp: Int = 0
        private set

    var expToRank: ExpToRank = ExpToRank()
        private set

    val maxRank: Int
        get() = expToRank.numRanks

    val expToNextRank: Int
        get() {
            if (rank >= maxRank) {
                return 0
            }
            return expToRank.get(rank) - exp
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
        while (rank < maxRank && remaining > 0) {
            if (remaining < expToNextRank) {
                exp += remaining
                remaining = 0
            } else {
                remaining -= expToNextRank
                rankUp()
            }
        }
    }

    fun rankUp() {
        if (rank < maxRank) {
            exp = 0
            rank += 1
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
        expToRank = json.fromJson(ExpToRank::class.java, expJson)

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

    fun saveState(): GameSaveData {
        // Save game state.
        val saveData = GameSaveData(this)
        return saveData
    }

    fun loadState(saveData: GameSaveData, ui: Ui) {
        // Load game state.
        //saveData.applyTo(this)
        // Load rank and experience.
        rank = saveData.rank
        exp = saveData.exp

        // Load resources.
        biscuits = saveData.biscuits
        biscuitsEarned = saveData.biscuitsEarned
        eclairs = saveData.eclairs
        eclairsEarned = saveData.eclairsEarned
        cupcakes = saveData.cupcakes
        cupcakesEarned = saveData.cupcakesEarned
        pies = saveData.pies
        piesEarned = saveData.piesEarned

        // Load owned products and upgrades.
        for ((name, owned) in saveData.products) {
            if (owned <= 0) {
                continue
            }
            val product = products.find { it.name == name }
            product?.owned = owned
        }
        for ((name, purchased) in saveData.upgrades) {
            if (!purchased) {
                continue
            }
            val upgrade = upgrades.find { it.name == name }
            upgrade?.purchased = true
            upgrade?.applyEffects(this)
        }

        // Load event info.
        for ((name, completedOnce) in saveData.events) {
            if (!completedOnce) {
                continue
            }
            val event = eventManager.events.find { it.name == name }
            event?.completedOnce = true
        }
        // Load active event info.
        for ((name, timeRemaining) in saveData.activeEvents) {
            val event = eventManager.events.find { it.name == name }
            if (event != null) {
                event.timeRemaining = timeRemaining
                eventManager.activeEvents.add(event)
            }
        }
        eventManager.activeEvents.sortBy { it.timeRemaining }
        // Add active events to UI.
        for ((i, event) in eventManager.activeEvents.withIndex()) {
            ui.addEvent(event, i)
        }
        eventManager.eventTimer = saveData.eventTimer
    }
}

class ExpToRank {
    var expToRank: ArrayList<Int> = ArrayList()
        private set

    val numRanks: Int
        get() = expToRank.size

    fun get(rank: Int): Int {
        return expToRank[rank]
    }
}