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
        val components = this.text.substring(2, this.text.length - 2).trim().split(" ")
        if (components.size < 4) {
            throw Exception("Parse error at : '${this.text}'")
        }
        this.lhs = components[1]
        this.operation = components[2]
        this.rhs = components[3]
    }

    override fun render(context: Context): String {
        // check for variables in context
        if (!context.containsVariable(this.lhs) ||
                !context.containsVariable(this.rhs)) {
            throw java.lang.Exception("Check for variables in '${this.text}'. Variable was not found in context.")
        }

        val result = StringBuilder(500)
        // run the true part
        if (this.op(context)) {
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

    private fun op(context: Context): Boolean {
        val lhsNum = context.getVariable(this.lhs)
        val rhsNum = context.getVariable(this.rhs)

        if (lhsNum !is Number ||
                rhsNum !is Number)
            throw Exception("Check ${this.lhs} and ${this.rhs} whether they are numbers in expression: ${this.text}")

        val result = lhsNum.toDouble().compareTo(rhsNum.toDouble())

        return when (this.operation) {
            ">" -> result > 0
            "<" -> result <  0
            ">=" -> result >= 0
            "<=" -> result <= 0
            else -> false
        }
    }

    override fun toString(): String = "IfNode '${this.text}'"
}