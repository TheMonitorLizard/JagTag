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

import me.jagrosh.jagtag.Method
import me.jagrosh.jagtag.Parser
import me.jagrosh.jagtag.ParserBuilder
import me.jagrosh.jagtag.libraries.*

fun ParserBuilder.addDefaultLibraries() : ParserBuilder = addDefaultLibraries {}

infix inline fun ParserBuilder.addDefaultLibraries(lazy: DefaultLibraryPack.() -> Unit) : ParserBuilder = with(DefaultLibraryPack())
{
    lazy()
    if(arguments)  this@addDefaultLibraries.addMethods(Arguments.getMethods())
    if(functional) this@addDefaultLibraries.addMethods(Functional.getMethods())
    if(strings)    this@addDefaultLibraries.addMethods(Strings.getMethods())
    if(variables)  this@addDefaultLibraries.addMethods(Variables.getMethods())
    this@addDefaultLibraries
}

data class DefaultLibraryPack(
        var arguments : Boolean = true, var functional : Boolean = true,
        var strings : Boolean = true, var variables : Boolean = true) {
    infix inline fun arguments(lazy: () -> Boolean) { arguments = lazy() }
    infix inline fun functional(lazy: () -> Boolean) { functional = lazy() }
    infix inline fun strings(lazy: () -> Boolean) { strings = lazy() }
    infix inline fun variables(lazy: () -> Boolean) { variables = lazy() }
}

infix inline fun ParserBuilder.addMethod(lazy: () -> Method) : ParserBuilder = addMethod(lazy())

infix inline fun ParserBuilder.addMethods(lazy: ArrayList<Method>.() -> Unit) : ParserBuilder = with(ArrayList<Method>()) {
    lazy()
    this@addMethods.addMethods(this)
}

infix fun ParserBuilder.newMethod(lazy: KMethod.() -> Unit) : ParserBuilder {
    this.addMethod(method(lazy))
    return this
}

inline fun parser(init: ParserBuilder.() -> Unit) : Parser = with(ParserBuilder())
{
    init()
    build()
}