package arrow_playground

import arrow.fx.coroutines.ComputationPool
import arrow.fx.coroutines.evalOn
import kotlinx.coroutines.runBlocking
import org.junit.Test

class REF_TRANSPARENCYKtTest
{


    fun `p1 with duplication`(): Int {

        val result1: Int = doSomething(3)
        val result2: Int = doSomething(3)

        val total = result1 + result2;

        return total;
    }

    fun `p1 refactored`(): Int {

        val result: Int = doSomething(3)

        val total = result + result;

        return total;
    }

    fun `p2 with duplication`(): Int {

        val result1: Int = runBlocking { `doSomething suspended`(3) }
        val result2: Int = runBlocking { `doSomething suspended`(3) }

        val total = result1 + result2;

        return total;
    }


    fun `p2 refactored`(): Int {

       val total: Int = runBlocking { `doSomething suspended`(3) + `doSomething suspended`(3) }

       //  val total: Int = runBlocking { `doSomething suspended`(3) + `doSomething suspended`(3) }

        return total;
    }

    fun `prr refactored`(): Int {

        val total: Int =
            runBlocking {
                evalOn(ComputationPool) { `doSomething suspended`(3) } + evalOn(ComputationPool) { `doSomething suspended`(3) }

            }


        return total

    }

    fun `p3 with duplication`(): Int {

        val result1: Int =  doSomething3(3)
        val result2: Int =  doSomething3(3)

        val total = result1 + result2;

        return total;
    }

    fun `p3 refactored`(): Int {

      /*  val r = doSomething3
        val total: Int = (r + r)(3)*/

        val total: Int = (doSomething3 + doSomething3)(3)

        return total;
    }

    fun `p3 other refactored`(): Int {

       val r = doSomething3
       val total: Int = (r + r )(3)

        return total;
    }

private operator fun  ((Int) -> Int).plus(other: (Int) -> Int) ={

    input: Int -> this(input) + other(input)
}

    @Test
    fun compareResults() {

        val `p1 with duplication`               = executeProgram( { `p1 with duplication`() })
        val `p1 refactored`                     = executeProgram( { `p1 refactored`() })

        val p2_1 = executeProgram { `p2 with duplication`() }
        val p2_2 = executeProgram { `p2 refactored`() }

        val p3_1 = executeProgram { `p3 with duplication`() }
        val p3_2 = executeProgram { `p3 refactored`() }
        val p3_3 = executeProgram { `p3 other refactored`() }


        val pr = executeProgram { `prr refactored`() }

        println("p1 with duplication result: $`p1 with duplication`")
        println("p1 refactored result:  $`p1 refactored`")
        println()

        println("p2 with duplication result: $p2_1")
        println("p2 refactored result:  $p2_2")
        println()

        println("p3 with duplication result: $p3_1")
        println("p3 refactored result:  $p3_2")
        println("p3 other result:  $p3_3")
        println()

        println("pr other result:  $pr")
        println()



    }

    private fun executeProgram(f: () -> Int): Int {
        resetState()

        return f();
    }




    suspend fun `doSomething suspended`(input: Int): Int {
        return doSomething(input)
    }

    val doSomething3 = {
            input: Int -> doSomething(input)
    }


    fun doSomething(input: Int): Int {
        if (availableSeats >= input)
        {
            availableSeats= availableSeats -input
            return input
        }
        else return 0

    }

    val A_CONST = 4
    var availableSeats = A_CONST

    private fun resetState() {
        availableSeats = A_CONST
    }


}
