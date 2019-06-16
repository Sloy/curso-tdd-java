class CoffeeMachine(private val drinkMaker: DrinkMaker,
                    val commandsRepository: CommandsRepository,
                    val reportOutput: ReportOutput) {

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

    fun orangeJuice() {
        orderState = orderState.copy(drink = DrinkType.ORANGE_JUICE)
    }

    fun addSugar() {
        orderState = orderState.copy(sugar = orderState.sugar + 1)
    }

    fun extraHot() {
        orderState = orderState.copy(extraHot = true)
    }

    fun make() {
        val command = buildCommand(orderState)
        when (command) {
            is Command.ValidOrder -> {
                drinkMaker.execute(command.format())
                resetOrder()
                commandsRepository.storeOrderCommand(command)
            }
            is Command.MissingMoneyError -> drinkMaker.execute(command.format())
        }

    }

    fun printReport() {
        reportOutput.print(commandsRepository.buildReport())
    }

    private fun buildCommand(order: OrderState): Command {
        with(order) {
            if (drink == DrinkType.UNSELECTED) {
                return Command.IncompleteOrder()
            }

            if (insertedAmount < drink.price) {
                return Command.MissingMoneyError(insertedAmount - drink.price)
            }

            val serveExtraHot = orderState.extraHot && orderState.drink.canServeHot
            return Command.ValidOrder(drink, Math.min(sugar, MAX_SUGAR), serveExtraHot)
        }
    }

    private fun resetOrder() {
        orderState = OrderState()
    }
}

data class OrderState(val drink: DrinkType = DrinkType.UNSELECTED,
                      val sugar: Int = 0,
                      val extraHot: Boolean = false,
                      val insertedAmount: Double = 0.0)

private const val MAX_SUGAR = 2