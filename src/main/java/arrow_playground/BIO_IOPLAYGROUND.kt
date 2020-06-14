package arrow_playground
import arrow.core.Either
import arrow.core.Right
import kotlinx.coroutines.runBlocking

inline class Id(val id: Long)
object PersistenceError

data class User(val email: String, val name: String)
data class ProcessedUser(val id: Id, val email: String, val name: String)

suspend fun fetchUser(): Either<PersistenceError, User> =
    Right(User("simon@arrow-kt.io", "Simon"))

suspend fun User.process(): Either<PersistenceError, ProcessedUser> =
    if (email.contains(Regex("^(.+)@(.+)$"))) Right(
        ProcessedUser(
            Id(1),
            email,
            name
        )
    )
    else Either.Left(PersistenceError)


suspend fun myProgram(): Either<PersistenceError, ProcessedUser> =
    Either.fx {
        val user = !fetchUser()
        val userUpper = toUpperCaseU(user)
        val processed = !userUpper.process()
        processed
    }

fun toUpperCaseU(user: User) = user.copy(name = user.name.toUpperCase())

fun main() {

    val runBlocking = runBlocking { myProgram() }

    val output = runBlocking.fold(
        { error -> "error $error" },
        { processedUser -> "processedUser $processedUser" })

    println(output)
}
/*

fun ioProgram(): IO<Either<PersistenceError, ProcessedUser>> =
    IO.fx {
        val res = !IO.effect { fetchUser() }

        !res.fold({ error ->
            IO.just(Either.Left(error))
        }, { user ->
            IO.effect { user.process() }
        })
    }

// Or unwrapped in `suspend`
suspend suspendedIOProgram(): Either<PersistenceError, ProcessedUser> =
ioProgram().suspended()*/
