package com.mxr.template

/**
 * This class is used to store the variables which are used during
 * content generation
 */
class Context {
    private val store = mutableMapOf<String, Any>()

    fun addVariable(name: String, parameter: Any) {
        this.store[name] = parameter
    }

    fun containsVariable(variableName: String): Boolean {
        return this.store.contains(variableName)
    }

    fun getVariable(name: String): Any? {
        return this.store[name]
    }
}