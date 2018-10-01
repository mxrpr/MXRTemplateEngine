package com.mxr.template.nodes

import com.mxr.template.Context


/**
 * EachNode implementation
 * {% each items %}
 *     {{it}}
 * {% end %}
 */
class EachNode(text: String) : Node(text) {

    private val loopVariableName: String

    init {
        this.hasScope = true
        this.loopVariableName = this.text.substring(2, this.text.length - 2).split(" ")[1]
    }

    override fun render(context: Context): String {
        if (!context.containsVariable(this.loopVariableName))
            throw Exception("No variable with name '${this.loopVariableName}' in Context. Expression: '${this.text}'")

        val loopVar = context.getVariable(this.loopVariableName) as? Array<*>
                ?: throw Exception("No variable with name '${this.loopVariableName}' in Context. Expression: '${this.text}'")

        val content = StringBuilder(1000)
        for (value in loopVar) {
            context.addVariable("it", value!!)

            for (child in this.childrens) {
                content.append(child.render(context))
            }
        }

        return content.toString()
    }
}