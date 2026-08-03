package acekode.debtshare

class Greeting {
    private val platform: Platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
