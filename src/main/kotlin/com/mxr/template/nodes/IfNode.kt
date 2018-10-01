package com.mxr.template.nodes

import com.mxr.template.Context
import java.lang.StringBuilder

/**
 *
 * IfNode class implementation
 *
 * Example:
 * {% if var > var2 %}
 */
class IfNode(text: String) : Node(text) {
    private val lhs: String
    private val operation: String
    private val rhs: String

    init {
        this.hasScope = true
        val componens = this.text.substring(2, this.text.length - 2).trim().split(" ")
        if (componens.size < 4) {
            throw Exception("Parse error at : '${this.text}'")
        }
        this.lhs = componens[1]
        this.operation = componens[2]
        this.rhs = componens[3]
    }

    override fun render(context: Context): String {
        // check for variables in context
        if (!context.containsVariable(this.lhs) ||
                !context.containsVariable(this.lhs)) {
            throw java.lang.Exception("Check for variables in '${this.text}'")
        }

        val result = StringBuilder(500)
        // run the true parst
        if (this.op()) {
            for (child in this.childrens) {
                if (child is ElseNode)
                    break
                result.append(child.render(context))
            }
        }
        // run the else part from the ElseIf element
        else {
            var canRun = false
            for (child in this.childrens) {
                if (child is ElseNode)
                    canRun = true
                if (canRun)
                    result.append(child.render(context))
            }
        }


        return result.toString()
    }

    private fun op(): Boolean {
        return when (this.operation) {
            ">" -> this.lhs > this.rhs
            "<" -> this.lhs < this.rhs
            ">=" -> this.lhs >= this.rhs
            "<=" -> this.lhs <= this.rhs
            else -> false
        }
    }

    override fun toString(): String = "IfNode '${this.text}'"
}