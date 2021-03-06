/*
 * =========================================================================================
 * Copyright © 2013 the kamon project <http://kamon.io/>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * =========================================================================================
 */

package kamon

import scala.annotation.tailrec
import com.typesafe.config.Config

package object metrics {

  case class HdrPrecisionConfig(highestTrackableValue: Long, significantValueDigits: Int)

  def extractPrecisionConfig(config: Config): HdrPrecisionConfig =
    HdrPrecisionConfig(config.getLong("highest-trackable-value"), config.getInt("significant-value-digits"))

  @tailrec def combineMaps[K, V](left: Map[K, V], right: Map[K, V])(valueMerger: (V, V) ⇒ V): Map[K, V] = {
    if (right.isEmpty)
      left
    else {
      val (key, rightValue) = right.head
      val value = left.get(key).map(valueMerger(_, rightValue)).getOrElse(rightValue)

      combineMaps(left.updated(key, value), right.tail)(valueMerger)
    }
  }
}
