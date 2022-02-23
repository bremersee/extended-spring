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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Objects;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.convert.converter.Converter;

/**
 * The mongo custom conversions filter test.
 *
 * @author Christian Bremer
 */
@ExtendWith({SoftAssertionsExtension.class})
class MongoCustomConversionsFilterTest {

  /**
   * All.
   */
  @Test
  void all() {
    MongoCustomConversionsFilter target = new MongoCustomConversionsFilter.All();
    Converter<?, ?> converter = mock(Converter.class);
    assertThat(target.test(converter))
        .isTrue();
  }

  /**
   * Class name prefix.
   *
   * @param softly the softly
   */
  @Test
  void classNamePrefix(SoftAssertions softly) {
    MongoCustomConversionsFilter target = new MongoCustomConversionsFilter.ClassNamePrefix(List.of(
        "org.bremersee."
    ));
    softly.assertThat(target.test(new ExampleConverter()))
        .isTrue();

    target = new MongoCustomConversionsFilter.ClassNamePrefix(null);
    softly.assertThat(target.test(new ExampleConverter()))
        .isFalse();
  }

  private static class ExampleConverter implements Converter<Object, String> {

    @Override
    public String convert(Object source) {
      return Objects.toString(source);
    }
  }

}