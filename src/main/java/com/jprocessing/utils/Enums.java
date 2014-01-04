/*
 * Copyright (c) 2014 Vladislav Zablotsky
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jprocessing.utils;

/**
 *
 * @author rumatoest
 */
public class Enums {

    /**
     * Return enum value for ordinal (index) code.
     * 
     * @param enumClass You should provide enum.class first
     * @param code Enum index
     */
    public static <E extends Enum<E>> E getValueOf(Class<E> enumClass, int code) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.ordinal() == code) {
                return e;
            }
        }
        throw new IllegalArgumentException("No enum const "
                + enumClass.getName() + " for code \'" + code + "\'");
    }

    /**
     * Return enum value for provided string representation
     * 
     * @param enumClass You should provide enum.class first
     * @param valu String representasion of enum (toString() used)
     */
    public static <E extends Enum<E>> E getValueOf(Class<E> enumClass, String val) {
        for (E e : enumClass.getEnumConstants()) {
            if (e.toString().equals(val)) {
                return e;
            }
        }
        throw new IllegalArgumentException("No enum const "
                + enumClass.getName() + " for value \'" + val + "\'");
    }
}
