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
package org.apache.raft.util;

/**
 * Use {@link System#nanoTime()} as timestamps.
 *
 * This class takes care the possibility of numerical overflow.
 *
 * This class is thread safe.
 */
public class Timestamp implements Comparable<Timestamp> {
  private static final long NANOSECONDS_PER_MILLISECOND = 1000000;

  /** @return the latest timestamp. */
  public static Timestamp latest(Timestamp a, Timestamp b) {
    return a.compareTo(b) > 0? a: b;
  }

  private final long nanos;

  private Timestamp(long nanos) {
    this.nanos = nanos;
  }

  /** Construct a timestamp with the current time. */
  public Timestamp() {
    this(System.nanoTime());
  }

  public Timestamp addTimeMs(long ms) {
    return new Timestamp(nanos + ms*NANOSECONDS_PER_MILLISECOND);
  }

  /**
   * @return the elapsed time in milliseconds.
   *         If the timestamp is a future time,
   *         this method returns a negative value.
   */
  public long elapsedTimeMs() {
    final long d = System.nanoTime() - nanos;
    return d / NANOSECONDS_PER_MILLISECOND;
  }

  /**
   * Compare two timestamps.
   * This method takes care the possibility of numerical overflow.
   *
   * @see System#nanoTime()
   */
  @Override
  public int compareTo(Timestamp that) {
    final long d = this.nanos - that.nanos;
    return d > 0? 1: d == 0? 0: -1;
  }

  @Override
  public String toString() {
    return nanos + "ns";
  }
}