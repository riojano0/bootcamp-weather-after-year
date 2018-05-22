package com.montivero.poc.configuration;

import com.montivero.poc.resource.domain.exception.ExceptionResponse;
import com.montivero.poc.resource.domain.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ExceptionHandlerControllerConfiguration.class, Logger.class})
public class ExceptionHandlerControllerConfigurationTest {

    @Mock
    private Logger mockLogger;

    private ExceptionHandlerControllerConfiguration exceptionHandlerControllerConfiguration;

    @Before
    public void setUp() throws Exception {
        exceptionHandlerControllerConfiguration = new ExceptionHandlerControllerConfiguration();
        Whitebox.setInternalState(ExceptionHandlerControllerConfiguration.class, "logger", mockLogger);
    }


    @Test
    public void shouldReturnResponseEntityWhenInterceptAValidationException() throws Exception {
        ValidationException validationException = new ValidationException("testField", "testValue", "testMessage");

        ResponseEntity responseEntity = exceptionHandlerControllerConfiguration.validationExceptionHandler(validationException);

        ExceptionResponse exceptionResponse = (ExceptionResponse) responseEntity.getBody();
        assertThat(exceptionResponse.getKey(), is("testField"));
        assertThat(exceptionResponse.getValue(), is("testValue"));
        assertThat(exceptionResponse.getMessage(), is("testMessage"));
        verify(mockLogger).error("Validation Exception", validationException.getLocalizedMessage());
    }
}