package com.mxr.template.nodes

import com.mxr.template.Context
import com.mxr.RPNRunner

/**
 *
 * Expression node can contain variables, numbers and operations.
 * Example:
 * {{= {{var1}} + {{var2}} * ( {{var2}} / {{var1}}) }}
 *
 * */
class ExpressionNode(text: String): Node(text) {
   private val expression = text.substring(2, text.length-2)

    override fun render(context: Context): String {
        val regexp = "\\{\\{.+?\\}\\}".toRegex()
        val sequence = regexp.findAll(text)
        for (match in sequence) {
            val varName = match.value.substring(2, match.value.length-2)
            if (!context.containsVariable(varName)) {
                throw Exception("Variable $varName does not exists in context. Expression: $expression")
            }
            this.expression.replace(match.value, context.getVariable(varName).toString())
        }

        val expRunner = RPNRunner()
        return  expRunner.calculate(this.expression).toString()
    }

    override fun toString(): String = "ExpressionNode: ${this.expression}"
}