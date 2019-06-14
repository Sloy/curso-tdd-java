import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.startsWith
import org.junit.Before
import org.junit.Test

class CoffeeMachineTest {

    private lateinit var drinkMaker: DrinkMaker
    private lateinit var machine: CoffeeMachine

    @Before
    fun setUp() {
        drinkMaker = mock()
        machine = CoffeeMachine(drinkMaker)
    }

    @Test
    fun `should send message with not enough money when coffee is requested`() {
        machine.insertCoin(0.5)
        machine.coffee()
        machine.make()

        val captor = argumentCaptor<String>()
        verify(drinkMaker).execute(captor.capture())
        val command = captor.lastValue
        assertThat(command, startsWith("M:"))
        assertThat(command, containsString("0.1"))
    }

    @Test
    fun `should send coffee command when coffee button pressed and has enough money`() {
        machine.insertCoin(1.0)
        machine.coffee()
        machine.make()

        verify(drinkMaker).execute("C::")
    }

    @Test
    fun `should send tea command when tea button pressed and has enough money`() {
        machine.insertCoin(1.0)
        machine.tea()
        machine.make()

        verify(drinkMaker).execute("T::")
    }

    @Test
    fun `should send chocolate command when chocolate button pressed and has enough money`() {
        machine.insertCoin(1.0)
        machine.chocolate()
        machine.make()

        verify(drinkMaker).execute("H::")
    }

    @Test
    fun `should send 1 sugar with stick when sugar button pressed once`() {
        machine.insertCoin(1.0)
        machine.coffee()

        machine.addSugar()
        machine.make()

        verify(drinkMaker).execute("C:1:0")
    }

    @Test
    fun `should send 2 sugars with stick when sugar button pressed twice`() {
        machine.insertCoin(1.0)
        machine.coffee()
        machine.addSugar()
        machine.addSugar()
        machine.make()

        verify(drinkMaker).execute("C:2:0")
    }

    @Test
    fun `should send 2 sugars with stick when sugar button pressed three times`() {
        machine.insertCoin(1.0)
        machine.coffee()
        machine.addSugar()
        machine.addSugar()
        machine.addSugar()
        machine.make()

        verify(drinkMaker).execute("C:2:0")
    }

    @Test
    fun `should make a coffee and return error message when 2 coffees are selected`() {
        machine.insertCoin(1.0)
        machine.coffee()
        machine.make()
        machine.make()

        val captor = argumentCaptor<String>()
        verify(drinkMaker, times(1)).execute(captor.capture())
        assertThat(captor.firstValue, Matchers.`is`("C::"))
    }

    @Test
    fun `should make a coffee and return error message when 2 coffees are selected and money inserted`() {
        machine.insertCoin(1.0)
        machine.coffee()
        machine.make()
        machine.insertCoin(1.0)
        machine.make()

        val captor = argumentCaptor<String>()
        verify(drinkMaker, times(1)).execute(captor.capture())

        assertThat(captor.firstValue, Matchers.`is`("C::"))
    }
}
