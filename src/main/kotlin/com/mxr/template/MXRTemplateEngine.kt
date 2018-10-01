package com.mxr.template

import com.mxr.template.nodes.*
import java.io.File

/**
 * From an input string or file parses the
 * template and generates the output
 */
class MXRTemplateEngine(content: String) {

    private val VAR_TOKEN_START: String = "\\{\\{"
    private val VAR_TOKEN_END: String = "\\}\\}"
    private val BLOCK_TOKEN_START: String = "\\{%"
    private val BLOCK_TOKEN_END: String = "%\\}"
    private val COMMENT_TOKEN_START = "\\{\\{!"
    private val COMMENT_TOKEN_END = "\\}\\}"
    private val SECTION_TOKEN_START = "\\{\\{#"
    private val SECTION_TOKEN_END = "\\}\\}"
    private val INVERSE_SECTION_TOKEN_START = "\\{\\{^"
    private val INVERSE_SECTION_TOKEN_END = "\\}\\}"
    private val EXPRESSION_TOKEN_START = "\\{\\{="
    private val EXPRESSION_TOKEN_END = "\\}\\}"

    private val REGEXP = "<.+?>|$EXPRESSION_TOKEN_START.+?$EXPRESSION_TOKEN_END| " +
            "$INVERSE_SECTION_TOKEN_START.+?$INVERSE_SECTION_TOKEN_END|" +
            "$COMMENT_TOKEN_START.+?$COMMENT_TOKEN_END|" +
            "$SECTION_TOKEN_START.+?$SECTION_TOKEN_END|" +
            "$VAR_TOKEN_START.+?$VAR_TOKEN_END|" +
            "$BLOCK_TOKEN_START.+?$BLOCK_TOKEN_END|" +
            "[\\w\\s:;><|'\\/-=]+"

    val content = content

    constructor(file: File) : this(file.readText()) {
    }

    /**
     * Parse the content and return the generated file
     */
    fun parse(context: Context): String {
        val tokens = this.parseTokens(this.getStringTokens(this.content))
        val rootNode = this.buildAST(tokens)

        return rootNode.render(context)
    }

    private fun parseTokens(tokens: Array<String>): Array<Token> {
        var result = mutableListOf<Token>()
        for (token in tokens) {
            if (token.startsWith("{{#")) {
                result.add(Token(token, TokenType.OPEN_BLOCK_TOKEN))
            } else if (token.startsWith("{{^")) {
                result.add(Token(token, TokenType.OPEN_BLOCK_TOKEN))
            } else if (token.startsWith("{{/")) {
                result.add(Token(token, TokenType.CLOSE_BLOCK_TOKEN))
            } else if (token.startsWith("{{=")) {
                result.add(Token(token, TokenType.EXPRESSION_TOKEN))
            } else if (token.startsWith("{{!")) {
                result.add(Token(token, TokenType.COMMENT_TOKEN))
            } else if (token.startsWith("{{")) {
                result.add(Token(token, TokenType.VARIABLE_TOKEN))
            } else if (token.startsWith("{%") && token.contains("end")) {
                result.add(Token(token, TokenType.CLOSE_BLOCK_TOKEN))
            } else if (token.startsWith("{%")) {
                result.add(Token(token, TokenType.OPEN_BLOCK_TOKEN))
            } else {
                result.add(Token(token, TokenType.TEXT_TOKEN))
            }
        }

        return result.toTypedArray()
    }

    fun createNodeFromToken(token: Token): Node {
        if (token.tokenType == TokenType.TEXT_TOKEN)
            return TextNode(token.textFragment)

        else if (token.tokenType == TokenType.VARIABLE_TOKEN)
            return VariableNode(token.textFragment)

        else if (token.tokenType == TokenType.COMMENT_TOKEN)
            return CommentNode(token.textFragment)

        else if (token.tokenType == TokenType.EXPRESSION_TOKEN)
            return ExpressionNode(token.textFragment)

        else if (token.tokenType == TokenType.OPEN_BLOCK_TOKEN) {
            if (token.textFragment.contains("each")) {
                return EachNode(token.textFragment)
            } else if (token.textFragment.contains("for")) {
                return ForNode(token.textFragment)
            } else if (token.textFragment.contains("if")) {
                return IfNode(token.textFragment)
            } else if (token.textFragment.contains("else")) {
                return ElseNode(token.textFragment)
            } else if (token.textFragment.contains("{{#")) {
                return SectionNode(token.textFragment)
            } else if (token.textFragment.contains("{{^")) {
                return InvertedSectionNode(token.textFragment)
            }
        }

        throw Exception("Parse error at: ${token.textFragment}")
    }

    private fun buildAST(tokens: Array<Token>): RootNode {
        val rootNode = RootNode()
        val scopeStack = mutableListOf<Node>()
        for (token in tokens) {
            if (scopeStack.isEmpty())
                throw Exception("Too many end tags or something is not closed correctly: '$token'")
            val parentScope = scopeStack.last()
        }
        return rootNode
    }

    private fun getStringTokens(content: String): Array<String> {
        val result = mutableListOf<String>()
        val regexp = REGEXP.toRegex()
        val sequence = regexp.findAll(content)
        for (match in sequence) {
            result.add(match.value)
            println("-> ${match.value}")
        }
        return result.toTypedArray()
    }
}