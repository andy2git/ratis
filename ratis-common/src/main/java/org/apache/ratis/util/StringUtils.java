/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ratis.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

import java.util.Iterator;
import java.util.Locale;

public class StringUtils {
  public static final String[] EMPTY_STRING_ARRAY = {};

  /** Retains a weak reference to each string instance it has interned. */
  private static final Interner<String> WEAK_INTERNER = Interners.newWeakInterner();

  /**
   * Interns and returns a reference to the representative instance
   * for any of a collection of string instances that are equal to each other.
   * Retains weak reference to the instance,
   * and so does not prevent it from being garbage-collected.
   *
   * @param sample string instance to be interned
   * @return weak reference to interned string instance
   */
  public static String weakIntern(String sample) {
    return sample == null? null: WEAK_INTERNER.intern(sample);
  }

  /**
   * Splits the given comma separated {@link String}.
   * Each split value is trimmed.
   *
   * @param s a comma separated {@link String}, or possibly null.
   * @return the split strings, or an empty array if the given string is null.
   */
  public static String[] getTrimmedStrings(String s) {
    return s == null? EMPTY_STRING_ARRAY
        : (s = s.trim()).isEmpty()? EMPTY_STRING_ARRAY
        : s.split("\\s*,\\s*");
  }

  /** The same as String.format(Locale.ENGLISH, format, objects). */
  public static String format(final String format, final Object... objects) {
    return String.format(Locale.ENGLISH, format, objects);
  }

  public static String bytes2HexString(byte[] bytes) {
    Preconditions.checkNotNull(bytes);

    final StringBuilder s = new StringBuilder(2 * bytes.length);
    for(byte b : bytes) {
      s.append(format("%02x", b));
    }
    return s.toString();
  }

  public static boolean string2boolean(String s, boolean defaultValue) {
    if (s == null || s.isEmpty()) {
      return defaultValue;
    }

    if ("true".equalsIgnoreCase(s)) {
      return true;
    } else if ("false".equalsIgnoreCase(s)) {
      return false;
    } else {
      return defaultValue;
    }
  }

  /**
   *  @return the next string in the iteration right after the given string;
   *          if the given string is not in the iteration, return the first string.
   */
  public static String next(final String given, final Iterable<String> iteration) {
    Preconditions.checkNotNull(given);
    Preconditions.checkNotNull(iteration);
    final Iterator<String> i = iteration.iterator();
    Preconditions.checkArgument(i.hasNext());

    final String first = i.next();
    for(String current = first; i.hasNext(); ) {
      final String next = i.next();
      if (given.equals(current)) {
        return next;
      }
      current = next;
    }
    return first;
  }
}
