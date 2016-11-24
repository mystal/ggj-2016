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

    var products: ArrayList<ProductSaveData> = ArrayList()
    var upgrades: ArrayList<UpgradeSaveData> = ArrayList()

    var events: ArrayList<EventSaveData> = ArrayList()
    var activeEvents: ArrayList<ActiveEventSaveData> = ArrayList()
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
        //products = game.products.filter { it.owned > 0 }.map { ProductSaveData(it.name, it.owned) }
        for (product in game.products) {
            if (product.owned > 0) {
                products.add(ProductSaveData(product.name, product.owned))
            }
        }
        for (upgrade in game.upgrades) {
            if (upgrade.purchased) {
                upgrades.add(UpgradeSaveData(upgrade.name, true))
            }
        }

        // Save event info.
        for (event in game.eventManager.events) {
            events.add(EventSaveData(event.name, event.completedOnce))
        }
        // Save active events, names, how long they have left.
        for (event in game.eventManager.activeEvents) {
            activeEvents.add(ActiveEventSaveData(event.name, event.timeRemaining))
        }
        eventTimer = game.eventManager.eventTimer
    }
}

data class ProductSaveData(var name: String = "", var owned: Int = 0)
data class UpgradeSaveData(var name: String = "", var purchased: Boolean = false)
data class EventSaveData(var name: String = "", var completedOnce: Boolean = false)
data class ActiveEventSaveData(var name: String = "", var timeRemaining: Float = 0f)
