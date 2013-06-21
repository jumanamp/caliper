/*
 * Copyright (C) 2011 Google Inc.
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

package com.google.caliper.worker;

import com.google.inject.Inject;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * The {@link Worker} for the {@code AllocationInstrument}.  This class invokes the benchmark method
 * a few times, with varying numbers of reps, and computes the number of object allocations and the
 * total size of those allocations.
 */
public final class MacrobenchmarkAllocationWorker implements Worker {
  private final RecordingAllocationSampler recorder;

  @Inject MacrobenchmarkAllocationWorker(RecordingAllocationSampler recorder) {
    this.recorder = recorder;
  }

  @Override public void measure(Object benchmark, Method method,
      Map<String, String> options, WorkerEventLog log) throws Exception {
    // do one initial measurement and throw away its results
    log.notifyWarmupPhaseStarting();
    measureAllocations(benchmark, method);
    log.notifyMeasurementPhaseStarting();
    while (true) {
      log.notifyMeasurementStarting();
      AllocationStats measurement = measureAllocations(benchmark, method);
      log.notifyMeasurementEnding(measurement.toMeasurements());
    }
  }

  private AllocationStats measureAllocations(Object benchmark, Method method) throws Exception {
    recorder.startRecording();
    method.invoke(benchmark);
    return recorder.stopRecording(1);
  }
}