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
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Objects;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

/**
 * The mongo custom conversions' autoconfiguration test.
 *
 * @author Christian Bremer
 */
@ExtendWith(SoftAssertionsExtension.class)
class MongoCustomConversionsAutoConfigurationTest {

  /**
   * Init.
   */
  @Test
  void init() {
    MongoProperties properties = new MongoProperties();
    properties.setEnableCustomConversions(false);
    properties.setAllowedCustomConverterPrefixes(List.of());
    properties = spy(properties);
    MongoCustomConversionsAutoConfiguration target = new MongoCustomConversionsAutoConfiguration(
        properties);
    target.init();
    verify(properties, atLeast(1)).isEnableCustomConversions();
    verify(properties, atLeast(1)).getAllowedCustomConverterPrefixes();
  }

  /**
   * Mongo custom conversions filter.
   *
   * @param softly the softly
   */
  @Test
  void mongoCustomConversionsFilter(SoftAssertions softly) {
    MongoProperties properties = new MongoProperties();
    properties.setAllowedCustomConverterPrefixes(List.of());
    MongoCustomConversionsAutoConfiguration target = new MongoCustomConversionsAutoConfiguration(
        properties);
    MongoCustomConversionsFilter actual = target.mongoCustomConversionsFilter();
    softly.assertThat(actual)
        .isInstanceOf(MongoCustomConversionsFilter.All.class);

    properties.setAllowedCustomConverterPrefixes(List.of("org.bremersee."));
    target = new MongoCustomConversionsAutoConfiguration(
        properties);
    actual = target.mongoCustomConversionsFilter();
    softly.assertThat(actual)
        .isInstanceOf(MongoCustomConversionsFilter.ClassNamePrefix.class);
  }

  /**
   * Custom conversions.
   */
  @Test
  void customConversions() {
    MongoProperties properties = new MongoProperties();
    properties.setEnableCustomConversions(true);
    properties.setAllowedCustomConverterPrefixes(List.of());
    MongoCustomConversionsAutoConfiguration target = new MongoCustomConversionsAutoConfiguration(
        properties);
    MongoCustomConversions actual = target.customConversions(
        target.mongoCustomConversionsFilter(),
        List.of(new ExampleMongoCustomConversionsProvider())
    );
    assertThat(actual)
        .isNotNull();
  }

  private static class ExampleMongoCustomConversionsProvider
      implements MongoCustomConversionsProvider {

    @Override
    public List<Converter<?, ?>> getCustomConversions() {
      return List.of(new ExampleConverter());
    }
  }

  private static class ExampleConverter implements Converter<Object, String> {

    @Override
    public String convert(Object source) {
      return Objects.toString(source);
    }
  }

}