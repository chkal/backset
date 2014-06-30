package de.chkal.backset.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class ConfigManagerTest {

  private final DefaultConfigManager configManager = new DefaultConfigManager();

  @Test
  public void shouldReturnNullForUnknownConfig() {

    FoobarConfig config = configManager.getConfig("foobar", FoobarConfig.class);
    assertThat(config).isNull();

  }

  @Test
  public void shouldReturnCorrectConfigForMatchingName() throws IOException {

    configManager.addConfig(new StringReader("" +
        "foobar:\n" +
        "  item: Candy\n" +
        "  count: 5"));

    FoobarConfig config = configManager.getConfig("foobar", FoobarConfig.class);
    assertThat(config).isNotNull();
    assertThat(config.getItem()).isEqualTo("Candy");
    assertThat(config.getCount()).isEqualTo(5);

  }

  @Test
  public void shouldNotReturnConfigWithOtherName() throws IOException {

    configManager.addConfig(new StringReader("" +
        "foobar:\n" +
        "  item: Candy\n" +
        "  count: 5"));

    FoobarConfig config = configManager.getConfig("other", FoobarConfig.class);
    assertThat(config).isNull();

  }

}
