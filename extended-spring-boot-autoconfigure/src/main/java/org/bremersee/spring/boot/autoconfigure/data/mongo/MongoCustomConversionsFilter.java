/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bremersee.spring.boot.autoconfigure.data.mongo;

import static java.util.Objects.nonNull;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import org.springframework.core.convert.converter.Converter;

/**
 * The interface Mongo custom conversions filter.
 *
 * @author Christian Bremer
 */
public interface MongoCustomConversionsFilter extends Predicate<Converter<?, ?>> {

  /**
   * The type All.
   */
  class All implements MongoCustomConversionsFilter {

    @Override
    public boolean test(Converter<?, ?> converter) {
      return nonNull(converter);
    }
  }

  /**
   * The type Class name prefix.
   */
  class ClassNamePrefix implements MongoCustomConversionsFilter {

    private final Set<String> allowedPrefixes;

    /**
     * Instantiates a new Class name prefix.
     *
     * @param allowedPrefixes the allowed prefixes
     */
    public ClassNamePrefix(Collection<String> allowedPrefixes) {
      if (nonNull(allowedPrefixes)) {
        this.allowedPrefixes = Set.copyOf(allowedPrefixes);
      } else {
        this.allowedPrefixes = Set.of();
      }
    }

    @Override
    public boolean test(Converter<?, ?> converter) {
      return allowedPrefixes
          .stream()
          .anyMatch(prefix -> converter.getClass().getName().startsWith(prefix));
    }

  }

}
