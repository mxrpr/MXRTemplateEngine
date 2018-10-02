package com.mxr.template.nodes

import com.mxr.template.Context
import com.mxr.RPNRunner
import com.mxr.ValueProvider

/**
 *
 * Expression node can contain variables, numbers and operations.
 * Example:
 * {{= {{var1}} + {{var2}} * ( {{var2}} / {{var1}}) }}
 *
 * */
internal class ExpressionNode(text: String): Node(text), ValueProvider {

    private val expression = text.substring(3, text.length-2).trim()
    private var context: Context? = null

    override fun render(context: Context): String {
        this.context = context
        val expRunner = RPNRunner(this)
        return  expRunner.calculate(this.expression).toString()
    }

    override fun getValue(variableName: String): Double {
        return (this.context?.getVariable(variableName.trim()) as Number).toDouble()
    }

    override fun toString(): String = "ExpressionNode: ${this.expression}"
}