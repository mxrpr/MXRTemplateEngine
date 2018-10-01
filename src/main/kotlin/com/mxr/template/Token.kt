package com.mxr.template

enum class TokenType {
    NONE_TOKEN,
    TEXT_TOKEN,
    VARIABLE_TOKEN,
    OPEN_BLOCK_TOKEN,
    CLOSE_BLOCK_TOKEN,
    COMMENT_TOKEN,
    EXPRESSION_TOKEN
}

class Token(textFragment: String, tokenType: TokenType) {
    val textFragment : String = textFragment
    val tokenType: TokenType = tokenType

    override fun toString(): String = "${this.textFragment}"

}