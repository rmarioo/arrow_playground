package arrow_playground

import arrow.core.Either
import arrow.core.Either.Companion.conditionally
import kotlinx.coroutines.runBlocking

data class CreditCard(val amount: Int)
data class Coffee(val cost: Int =2)
object PlafondNotEnoughError

val INITIAL_PLAFOND = 5
var plafond = INITIAL_PLAFOND


suspend fun buyCoffee(): () -> Either<PlafondNotEnoughError, Coffee> = {

    buyCoffeeWithSideEffect()

}


val buyCoffeSuspendedVar = suspend {

    buyCoffeeWithSideEffect()
}


private fun buyCoffeS() = suspend {

    buyCoffeeWithSideEffect()

}


suspend fun buyCoffees(n: Int): Either<PlafondNotEnoughError, List<Coffee>> {
    return Either.fx {

        val v = { buyCoffeeWithSideEffect() }
        val doAllCoffees = List(n) { !v() }
        doAllCoffees
    }
}


private fun buyCoffeeWithSideEffect(): Either<PlafondNotEnoughError, Coffee> {
    val creditCard =
        CreditCard(plafond)
    val coffe = Coffee()
    return conditionally(creditCard.amount > coffe.cost,
        { PlafondNotEnoughError },
        { plafond = plafond - coffe.cost; coffe })
}

fun main() {

    println("1 plafond $plafond")

    val program = runBlocking { buyCoffee() }
    val res1 = program()
    val res2 = program()
    val res3 = program()

    println(resultToString(res3))
    println("2 plafond $plafond")
    resetPlafond()

    println("---------------------")
    println("---------------------")
    println("---------------------")

  //  val res = runBlocking { buyCoffee(creditCard) }
    println("3 plafond $plafond")
    val res = runBlocking { buyCoffees(3) }

    println(resultToStringList(res))
    println("4 plafond $plafond")

}

private fun resultToStringList(either: Either<PlafondNotEnoughError, List<Coffee>>) =
    either.fold({ e -> "error $e" }, { v -> "result $v" })

private fun resultToString(either: Either<PlafondNotEnoughError, Coffee>) =
    either.fold({ e -> "error $e" }, { v -> "result $v" })




private fun resetPlafond() {
    plafond =
        INITIAL_PLAFOND
}


