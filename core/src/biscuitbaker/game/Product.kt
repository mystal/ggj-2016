package biscuitbaker.game

class Product(name: String, price: Long, bps: Double) {
    public var owned: Int = 0
    public var name: String = name
    public var price: Long = price

    // TODO: Make this a Long
    public var bps: Double = bps
    public var displayCondition: Long = 0
}