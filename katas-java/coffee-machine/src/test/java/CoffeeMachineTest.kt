import com.nhaarman.mockitokotlin2.*
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.startsWith
import org.junit.Before
import org.junit.Test

class CoffeeMachineTest {

    private lateinit var drinkMaker: DrinkMaker
    private lateinit var machine: CoffeeMachine
    private lateinit var reportOutput: ReportOutput

    @Before
    fun setUp() {
        drinkMaker = mock()
        reportOutput = mock()
        machine = CoffeeMachine(drinkMaker, MemoryCommandsRepository(), reportOutput)
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
    fun `should send juice command when orange juice button pressed and has enough money`() {
        machine.insertCoin(1.0)
        machine.orangeJuice()
        machine.make()

        verify(drinkMaker).execute("O::")
    }

    @Test
    fun `should send hot command for hot beverages`() {
        machine.insertCoin(1.0)
        machine.coffee()
        machine.extraHot()
        machine.make()

        verify(drinkMaker).execute("Ch::")
    }

    @Test
    fun `should not send hot command for cold beverages`() {
        machine.insertCoin(1.0)
        machine.orangeJuice()
        machine.extraHot()
        machine.make()

        verify(drinkMaker).execute("O::")
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

    @Test
    fun `should print report`() {
        machine.insertCoin(1.0)
        machine.coffee()
        machine.make()
        machine.insertCoin(1.0)
        machine.coffee()
        machine.make()
        machine.insertCoin(1.0)
        machine.chocolate()
        machine.make()
        machine.insertCoin(1.0)
        machine.chocolate()
        machine.make()
        machine.insertCoin(1.0)
        machine.tea()
        machine.make()

        machine.printReport()

        val outputCaptor = argumentCaptor<String> {
            verify(reportOutput).print(capture())
        }
        val report = outputCaptor.firstValue
        assertThat(report, containsString("2 COFFEE"))
        assertThat(report, containsString("2 CHOCOLATE"))
        assertThat(report, containsString("1 TEA"))
        assertThat(report, containsString("2.6"))

    }
}
