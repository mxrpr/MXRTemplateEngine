package com.mxr.template.nodes

import com.mxr.template.Context

/**
 *
 * The most basic tag type is the variable. A {{name}} tag in a basic template will
 * try to find the name key in the current context. If there is no name key,
 * nothing will be rendered.
 */
class VariableNode(text: String) : Node(text) {

    private val variable: String = text.substring(2, text.length - 2)

    override fun render(context: Context): String {
        // check if the context contains the variable
        if (!context.containsVariable(this.variable))
            throw Exception("Variable '${this.variable}' was not found in context")

        return this.text.replace(this.text, context.getVariable(this.variable).toString())
    }
}