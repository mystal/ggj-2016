package biscuitbaker.game

import java.util.*


class GameSaveData(game: Game) {
    var level: Int = 0

    var exp: Int = 0

    var biscuits: Double = 0.0
    var biscuitsEarned: Double = 0.0

    var eclairs: Double = 0.0
    var eclairsEarned: Double = 0.0

    var cupcakes: Double = 0.0
    var cupcakesEarned: Double = 0.0

    var pies: Double = 0.0
    var piesEarned: Double = 0.0

    var ownedProducts: HashMap<String, Int> = HashMap()
    var purchasedUpgrades: ArrayList<String> = ArrayList()

    init {
        // Store resource, product, and upgrade info.
        level = game.level
        exp = game.exp

        biscuits = game.biscuits
        biscuitsEarned = game.biscuitsEarned
        eclairs = game.eclairs
        eclairsEarned = game.eclairsEarned
        cupcakes = game.cupcakes
        cupcakesEarned = game.cupcakesEarned
        pies = game.pies
        piesEarned = game.piesEarned

        for (product in game.products) {
            if (product.owned > 0) {
                ownedProducts[product.name] = product.owned
            }
        }
        for (upgrade in game.upgrades) {
            if (upgrade.purchased) {
                purchasedUpgrades.add(upgrade.name)
            }
        }
    }
}
