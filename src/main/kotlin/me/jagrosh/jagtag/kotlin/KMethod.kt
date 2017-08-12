/*
 * Copyright 2017 John Grosh (jagrosh).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused")
package me.jagrosh.jagtag.kotlin

import me.jagrosh.jagtag.Environment
import me.jagrosh.jagtag.Method
import me.jagrosh.jagtag.ParseBiFunction
import me.jagrosh.jagtag.ParseFunction

/**
 * Creates a new [Method] block, returning the method
 * created from it.
 *
 * This will throw [IllegalArgumentException]s if neither
 * a [ParseFunction] or [ParseBiFunction] is specified
 * in the block.
 *
 * @param init The block that initializes the
 * returning [Method].
 *
 * @return The [Method] created from the [init] block.
 *
 * @throws IllegalArgumentException if neither a
 * [ParseFunction] or [ParseBiFunction] is specified
 * in the [init] block.
 */
@Throws(IllegalArgumentException::class)
fun method(init: KMethod.() -> Unit) : Method = with(KMethod())
{
    init()
    try { build() } catch (e: IllegalArgumentException) { throw e }
}

class KMethod internal constructor()
{
    var name : String = ""
    var function : ParseFunction? = null
    var biFunction : ParseBiFunction? = null

    @JvmField val splitters : ArrayList<String> = ArrayList()

    operator fun component1() = name
    operator fun component2() = function
    operator fun component3() = biFunction
    operator fun component4() = splitters

    @Throws(IllegalArgumentException::class)
    fun build() : Method
    {
        val (name, function, biFunction, splitters) = this@KMethod

        if(function!=null) {
            return if(biFunction != null)
                if(splitters.isNotEmpty())
                    Method(name, function, biFunction, *splitters.toTypedArray())
                else
                    Method(name, function, biFunction)
            else
                Method(name, function)
        }

        return if(biFunction != null)
            if(splitters.isNotEmpty())
                Method(name, biFunction, *splitters.toTypedArray())
            else
                Method(name, biFunction)
        else throw IllegalArgumentException("Must specify a Function and/or BiFunction")
    }

    infix inline fun name(lazy: () -> String) : KMethod { name = lazy(); return this }
    infix inline fun splitters(lazy: ArrayList<String>.() -> Unit) : KMethod { splitters.lazy(); return this }
    infix fun function(lazy: (Environment) -> String) : KMethod { function = ParseFunction(lazy); return this }
    infix fun biFunction(lazy: (Environment, Array<String>) -> String) : KMethod { biFunction = ParseBiFunction(lazy); return this }
}