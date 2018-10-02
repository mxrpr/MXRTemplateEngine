package com.mxr.template

internal enum class TokenType {
    NONE_TOKEN,
    TEXT_TOKEN,
    VARIABLE_TOKEN,
    OPEN_BLOCK_TOKEN,
    CLOSE_BLOCK_TOKEN,
    COMMENT_TOKEN,
    EXPRESSION_TOKEN
}

internal class Token(val textFragment: String, val tokenType: TokenType) {

    override fun toString(): String = this.textFragment
}