package com.mxr.template.nodes

import com.mxr.template.Context
import java.lang.StringBuilder

/**
 * ForNode
 * {% for items2 in items %}
 */
class ForNode(text: String) : Node(text) {
    private val variableName: String
    private val loopVariableName: String

    init {
        this.hasScope = true
        var components = this.text.substring(2, this.text.length -2).split(" ")
        if (components.size < 4) {
            throw Exception("Problem in parsing element: '${this.text}'")
        }
        this.variableName = components[4]
        this.loopVariableName = components[2]
    }

    override fun render(context: Context): String {
        if (!context.containsVariable(this.variableName)) {
            throw Exception("No variable in context '${this.variableName}' in expression '${this.text}'")
        }

        val _arr = context.getVariable(this.variableName)
        val content = StringBuilder(500)

        if (_arr is Array<*>) {
            for (element in _arr) {
                context.addVariable(this.loopVariableName, element!!)
                for (child in this.childrens) {
                    content.append(child.render(context))
                }
            }
        }
        else {
            throw Exception("'${this.variableName}' is not ar array in expression '${this.text}'")
        }

        return content.toString()
    }

}