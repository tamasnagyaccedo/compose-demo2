package tv.accedo.composedemo2.service

sealed interface TokenScope {

    data object Sam3 : TokenScope
    data object Jwt : TokenScope
    data object OpenID : TokenScope
}