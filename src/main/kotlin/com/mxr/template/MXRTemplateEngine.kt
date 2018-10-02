package com.mxr.template

import com.mxr.template.nodes.*
import java.io.File
import java.util.*


/**
 * From an input string or file parses the
 * template and generates the output
 */
class MXRTemplateEngine(private val content: String) {

    private val VAR_TOKEN_START: String = "\\{\\{"
    private val VAR_TOKEN_END: String = "\\}\\}"
    private val BLOCK_TOKEN_START: String = "\\{%"
    private val BLOCK_TOKEN_END: String = "%\\}"
    private val COMMENT_TOKEN_START = "\\{\\{\\!"
    private val COMMENT_TOKEN_END = "\\}\\}"
    private val SECTION_TOKEN_START = "\\{\\{#"
    private val SECTION_TOKEN_END = "\\}\\}"
    private val INVERSE_SECTION_TOKEN_START = "\\{\\{^"
    private val INVERSE_SECTION_TOKEN_END = "\\}\\}"
    private val EXPRESSION_TOKEN_START = "\\{\\{="
    private val EXPRESSION_TOKEN_END = "\\}\\}"

    private val REGEXP = "$EXPRESSION_TOKEN_START.+?$EXPRESSION_TOKEN_END| " +
            "$INVERSE_SECTION_TOKEN_START.+?$INVERSE_SECTION_TOKEN_END|" +
            "$COMMENT_TOKEN_START.+?$COMMENT_TOKEN_END|" +
            "$SECTION_TOKEN_START.+?$SECTION_TOKEN_END|" +
            "$VAR_TOKEN_START.+?$VAR_TOKEN_END|" +
            "$BLOCK_TOKEN_START.+?$BLOCK_TOKEN_END|" +
            "[!\"\\#\$%&'()*+,\\-\\./:;<=>?@\\[\\\\\\]^_`~ \\w\\s]+|" +
            "\\{[!\"\\#\$%&'()*+,\\-\\./:;<=>?@\\[\\\\\\]^_`~ \\w\\s]*|" +
            "[!\"\\#\$%&'()*+,\\-\\./:;<=>?@\\[\\\\\\]^_`~ \\w\\s]*\\}"
//            "({(?!\\{))?[!\"\\#\$%&'()*+,\\-\\./:;<=>?@\\[\\\\\\]^_`~ \\w\\s]*"
    // "\\{(?!\\{)"
    //"[\\w\\s:;\\.><|'\\/-=]+"


    /**
     * Constructor with file
     *
     * @param file  The file containing the text
     */
    constructor(file: File) : this(file.readText())

    /**
     * Parse the content and return the generated file
     */
    fun parse(context: Context): String {
        val tokens = this.parseTokens(this.getStringTokens(this.content))
        val rootNode = this.buildAST(tokens)

        return rootNode.render(context)
    }

    /*
     * Create Tokens from the string elements
     */
    private fun parseTokens(tokens: Array<String>): Array<Token> {
        val result = mutableListOf<Token>()
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

    /**
     * Process tokens and create nodes from them. Nodes will be
     * processed durin building the Abstract Syntax Tree
     */
    private fun createNodeFromToken(token: Token): Node {
        when (token.tokenType) {
            TokenType.TEXT_TOKEN        -> return TextNode(token.textFragment)
            TokenType.VARIABLE_TOKEN    -> return VariableNode(token.textFragment)
            TokenType.COMMENT_TOKEN     -> return CommentNode(token.textFragment)
            TokenType.CLOSE_BLOCK_TOKEN -> return EndNode(token.textFragment)
            TokenType.EXPRESSION_TOKEN  -> return ExpressionNode(token.textFragment)
            TokenType.OPEN_BLOCK_TOKEN  ->
                when {
                    token.textFragment.contains("each") -> return EachNode(token.textFragment)
                    token.textFragment.contains("for")  -> return ForNode(token.textFragment)
                    token.textFragment.contains("if")   -> return IfNode(token.textFragment)
                    token.textFragment.contains("else") -> return ElseNode(token.textFragment)
                    token.textFragment.contains("{{#")  -> return SectionNode(token.textFragment)
                    token.textFragment.contains("{{^")  -> return InvertedSectionNode(token.textFragment)
                }

        }

        throw Exception("Parse error at: ${token.textFragment}")
    }

    /**
     * Build the Abstract Syntax Tree
     */
    private fun buildAST(tokens: Array<Token>): RootNode {
        val rootNode = RootNode()
        val scopeStack = Stack<Node>()
        scopeStack.push(rootNode)

        for (token in tokens) {
            if (scopeStack.isEmpty())
                throw Exception("Too many end tags or something is not closed correctly: '$token'")
            val parentNode = scopeStack.peek()
            if (token.tokenType == TokenType.CLOSE_BLOCK_TOKEN) {
                // pop last element from stack
                scopeStack.pop()
                // check for end tags
            } else {
                val node = this.createNodeFromToken(token)
                parentNode.childrens.add(node)
                if (node.hasScope)
                    scopeStack.push(node)
            }
        }

        return rootNode
    }

    /**
     * Divide the content by tokens
     *
     * @param content The content
     * @return Array<String> Array of strng chunks
     */
    private fun getStringTokens(content: String): Array<String> {
        val result = mutableListOf<String>()
        val regexp = REGEXP.toRegex()
        val sequence = regexp.findAll(content)

        for (match in sequence)
            result.add(match.value)

        return result.toTypedArray()
    }
}