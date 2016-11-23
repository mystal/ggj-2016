package biscuitbaker.game

import java.util.*


class GameSaveData() {
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

    var activeEvents: ArrayList<String> = ArrayList()
    var eventTimer: Float = 0.0f

    constructor(game: Game) : this() {
        // Save level and experience.
        level = game.level
        exp = game.exp

        // Save resources.
        biscuits = game.biscuits
        biscuitsEarned = game.biscuitsEarned
        eclairs = game.eclairs
        eclairsEarned = game.eclairsEarned
        cupcakes = game.cupcakes
        cupcakesEarned = game.cupcakesEarned
        pies = game.pies
        piesEarned = game.piesEarned

        // Save owned products and upgrades.
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

        // Save event info.
        // TODO: Save active events, names, how long they have left.
        // TODO: Save which events have been completedOnce.
        for (event in game.eventManager.activeEvents) {
            activeEvents.add(event.name)
        }
        eventTimer = game.eventManager.eventTimer
    }

    fun applyTo(game: Game) {
    }
}
