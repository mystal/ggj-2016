package biscuitbaker.game

import java.util.*

class UpgradeInfo() {
    public var name: String = ""
    public var price: Long = 0
    //public var effect: Effect
    public var requirements: ProductRequirements? = null
    public var effects: UpgradeEffects? = null
}

class UpgradeEffects() {
    // TODO: What goes here? How do?!
}

class Upgrade(info: UpgradeInfo) {
    public var info: UpgradeInfo = info
        private set

    public var purchased: Boolean = false

    public val name: String
        get() = info.name

    public val price: Long
        get() = info.price

    // Whether to display this product in the store. Once visible, it
    // will always be visible.
    private var visible: Boolean = false

    // TODO: Add several levels of visibility
    // e.g. show shadowed item first, then full item once it is affordable
    public fun isVisible(game: Game): Boolean {
        // Once visible, always visible
        if (!visible) {
            visible = info.requirements?.isSatisfied(game) ?: true
        }
        return visible
    }
}

class UpgradeInfos() {
    public var upgrades: ArrayList<UpgradeInfo>? = null
}
