/*
 * Copyright (C) 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package examples;

import com.google.caliper.Param;
import com.google.caliper.Runner;
import com.google.caliper.SimpleBenchmark;
import com.google.common.collect.ImmutableList;
import java.util.Collection;

/**
 * Measures the various ways the JDK converts primitive doubles to Strings.
 */
public class PrimitiveDoubleToStringBenchmark extends SimpleBenchmark {

  @Param private double d;

  public static final Collection<Double> dValues = ImmutableList.of(
      Math.PI,
      -0.0d,
      Double.NEGATIVE_INFINITY,
      Double.NaN
  );

  public int timeStringFormat(int reps) {
    double value = d;
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy += String.format("%f", value).length();
    }
    return dummy;
  }

  public int timeToString(int reps) {
    double value = d;
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy += ((Double) value).toString().length();
    }
    return dummy;
  }

  public int timeStringValueOf(int reps) {
    double value = d;
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy += String.valueOf(value).length();
    }
    return dummy;
  }

  public int timeQuoteTrick(int reps) {
    double value = d;
    int dummy = 0;
    for (int i = 0; i < reps; i++) {
      dummy = ("" + value).length();
    }
    return dummy;
  }

  // TODO: remove this from all examples when IDE plugins are ready
  public static void main(String[] args) throws Exception {
    Runner.main(PrimitiveDoubleToStringBenchmark.class, args);
  }
}