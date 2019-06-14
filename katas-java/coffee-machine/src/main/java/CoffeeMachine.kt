class CoffeeMachine(private val drinkMaker: DrinkMaker) {

    private var orderState: OrderState = OrderState()

    fun insertCoin(coin: Double) {
        orderState = orderState.copy(insertedAmount = orderState.insertedAmount + coin)
    }

    fun coffee() {
        orderState = orderState.copy(drink = DrinkType.COFFEE)
    }

    fun tea() {
        orderState = orderState.copy(drink = DrinkType.TEA)
    }

    fun chocolate() {
        orderState = orderState.copy(drink = DrinkType.CHOCOLATE)
    }

    fun addSugar() {
        orderState = orderState.copy(sugar = orderState.sugar + 1)
    }

    fun make() {
        val command = buildCommand(orderState)
        when (command) {
            is Command.ValidOrder -> {
                drinkMaker.execute(command.format())
                resetOrder()
            }
            is Command.MissingMoneyError -> drinkMaker.execute(command.format())
        }

    }

    private fun buildCommand(order: OrderState): Command {
        with(order) {
            if (drink == DrinkType.UNSELECTED) {
                return Command.IncompleteOrder()
            }

            if (insertedAmount < drink.price) {

                return Command.MissingMoneyError(insertedAmount - drink.price)
            }

            return Command.ValidOrder(drink, Math.min(sugar, MAX_SUGAR))
        }
    }

    private fun resetOrder() {
        orderState = OrderState()
    }
}

data class OrderState(val drink: DrinkType = DrinkType.UNSELECTED,
                      val sugar: Int = 0,
                      val insertedAmount: Double = 0.0)

enum class DrinkType(val price: Double) {
    COFFEE(0.6), TEA(0.4), CHOCOLATE(0.5), UNSELECTED(0.0)

}

private const val MAX_SUGAR = 2