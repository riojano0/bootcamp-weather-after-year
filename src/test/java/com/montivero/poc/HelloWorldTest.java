package com.montivero.poc;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class HelloWorldTest {

    @Test
    public void shouldSaluteWithHelloWorld() {

        assertThat(HelloWorld.salute(), is("Hello World"));
    }
}