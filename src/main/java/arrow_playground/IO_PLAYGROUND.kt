package arrow_playground

import arrow.fx.coroutines.ComputationPool
import arrow.fx.coroutines.IOPool
import arrow.fx.coroutines.evalOn

suspend fun getThreadName(): String =
    Thread.currentThread().name

suspend fun main(): Unit {
    val t1 = evalOn(ComputationPool) { getThreadName() }
    val t2 = evalOn(IOPool) { getThreadName() }
    println("$t1 ~> $t2 ")
}

