package acekode.debtshare.domain

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String = "Hello, ${platform.name}!"
}

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
