package tv.accedo.composedemo2.service.impl

sealed class Token(val token: String) {
    override fun toString(): String = token
}

class Sam3Token(token: String) : Token(token)
class JwtToken(token: String) : Token(token)
class OpenIdToken(token: String) : Token(token)