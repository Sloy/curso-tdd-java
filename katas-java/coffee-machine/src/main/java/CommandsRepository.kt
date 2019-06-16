interface CommandsRepository {
    fun buildReport(): String
    fun storeOrderCommand(command: Command.ValidOrder)

}

class MemoryCommandsRepository : CommandsRepository {
    private val commands = mutableListOf<Command.ValidOrder>()
    override fun storeOrderCommand(command: Command.ValidOrder) {
        commands += command
    }

    override fun buildReport(): String {
        val drinkLines = DrinkType.values()
                .filter { it != DrinkType.UNSELECTED }
                .map {
                    "${commands.countDrink(it)} ${it.name}"
                }
        val totalLine = "Total earnings: ${commands.sumByDouble { it.drink.price }} €"

        return (drinkLines + totalLine).joinToString("\n")
    }
}


private fun List<Command.ValidOrder>.countDrink(type: DrinkType): Int {
    return this.count { it.drink == type }
}