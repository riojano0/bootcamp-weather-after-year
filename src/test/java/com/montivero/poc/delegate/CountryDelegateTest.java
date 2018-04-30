package com.montivero.poc.delegate;

import com.montivero.poc.client.CountryNameClient;
import com.montivero.poc.client.domain.groupKT.GroupKTCountry;
import com.montivero.poc.client.domain.groupKT.GroupKTResponse;
import com.montivero.poc.client.domain.groupKT.GroupKTRestResponse;
import com.montivero.poc.helper.GroupKTHelper;
import com.montivero.poc.helper.ResponseEntityHelper;
import com.montivero.poc.repository.CountryRepository;
import com.montivero.poc.resource.domain.Country;
import com.montivero.poc.resource.domain.Message;
import com.montivero.poc.transformer.CountryTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CountryDelegate.class, GroupKTHelper.class, ResponseEntityHelper.class})
public class CountryDelegateTest {

    private static final String COUNTRY_NAME = "Scadrial";
    private static final String COUNTRY_ISO2_CODE = "SC";
    private static final String COUNTRY_ISO3_CODE = "SCA";
    private static final String MESSAGE_VALUE_TRY_WITH_ISO_2_CODE_OR_ISO_3_CODE = "Try with Iso2Code or Iso3Code";
    private static final String MESSAGE_VALUE_STATE_SAVED = "Country Saved";
    private static final String MESSAGE_VALUE_STATE_ALREADY_SAVE = "Country Already Save";

    private Country country;
    private GroupKTCountry groupKTCountry;
    private ResponseEntity responseEntitySuccessful;
    private GroupKTRestResponse<GroupKTCountry> groupKTRestResponse;
    private GroupKTResponse<GroupKTCountry> groupKTResponse;

    @Mock
    private CountryNameClient mockCountryNameClient;
    @Mock
    private CountryTransformer mockCountryTransformer;
    @Mock
    private CountryRepository mockCountryRepository;

    private CountryDelegate countryDelegate;

    @Before
    public void setUp() {
        initMocks(this);
        mockStatic(GroupKTHelper.class);
        mockStatic(ResponseEntityHelper.class);
        countryDelegate = new CountryDelegate(mockCountryNameClient, mockCountryTransformer, mockCountryRepository);

        responseEntitySuccessful = new ResponseEntity<>("Successful", HttpStatus.OK);

        groupKTCountry = new GroupKTCountry();
        groupKTCountry.setName(COUNTRY_NAME);
        groupKTCountry.setAlpha3_code(COUNTRY_ISO3_CODE);
        groupKTCountry.setAlpha2_code(COUNTRY_ISO2_CODE);

        country = Country.builder()
                .name(COUNTRY_NAME)
                .shortName2(COUNTRY_ISO2_CODE)
                .shortName3(COUNTRY_ISO3_CODE)
                .build();

        groupKTRestResponse = new GroupKTRestResponse<>();
        groupKTRestResponse.setResult(groupKTCountry);
        groupKTResponse = new GroupKTResponse<>();
        groupKTResponse.setRestResponse(groupKTRestResponse);
        when(GroupKTHelper.getRestResponse(groupKTResponse)).thenReturn(groupKTRestResponse);
        when(GroupKTHelper.getResult(groupKTRestResponse)).thenReturn(groupKTCountry);
        when(mockCountryTransformer.transform(groupKTCountry)).thenReturn(country);
        when(ResponseEntityHelper.prepareResponseEntityForLocation(country)).thenReturn(responseEntitySuccessful);
    }

    @Test
    public void shouldGetAllTheCountriesFromClientWhenDatabaseNoHaveMatchAndAfterCallDatabaseForSaveTheResults() {
        List<Country> countries = Collections.singletonList(country);
        List<GroupKTCountry> groupKTCountries = Collections.singletonList(groupKTCountry);
        GroupKTRestResponse<List<GroupKTCountry>> groupKTRestResponse = new GroupKTRestResponse<>();
        groupKTRestResponse.setResult(groupKTCountries);
        GroupKTResponse<List<GroupKTCountry>> groupKTResponse = new GroupKTResponse<>();
        groupKTResponse.setRestResponse(groupKTRestResponse);
        when(mockCountryRepository.findAll()).thenReturn(null);
        when(mockCountryNameClient.getAllCountries()).thenReturn(groupKTResponse);
        when(GroupKTHelper.getRestResponse(groupKTResponse)).thenReturn(groupKTRestResponse);
        when(GroupKTHelper.getResult(groupKTRestResponse)).thenReturn(groupKTCountries);
        when(mockCountryTransformer.transformList(groupKTCountries)).thenReturn(countries);
        when(ResponseEntityHelper.prepareResponseEntityForList(countries)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = countryDelegate.getAllCountries();

        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryRepository).findAll();
        verify(mockCountryNameClient).getAllCountries();
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockCountryTransformer).transformList(groupKTCountries);
        verify(mockCountryRepository).saveAll(countries);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForList(countries);
    }

    @Test
    public void shouldGetAllTheCountriesFromDatabaseWhenHaveMatch() {
        List<Country> countries = Collections.singletonList(country);
        when(mockCountryRepository.findAll()).thenReturn(countries);
        when(ResponseEntityHelper.prepareResponseEntityForList(countries)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = countryDelegate.getAllCountries();

        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryRepository).findAll();
        verify(mockCountryNameClient, never()).getAllCountries();
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getRestResponse(any());
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getResult(any());
        verify(mockCountryTransformer, never()).transformList(anyList());
        verify(mockCountryRepository, never()).saveAll(countries);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForList(countries);
    }

    @Test
    public void shouldGetCountryWithIso2CodeFromClientWhenDatabaseNoHaveMatchAndAfterCallDatabaseForSaveTheResult() {
        when(mockCountryRepository.findByShortName2(COUNTRY_ISO2_CODE)).thenReturn(null);
        when(mockCountryNameClient.getCountryByIso2Code(COUNTRY_ISO2_CODE)).thenReturn(groupKTResponse);

        ResponseEntity responseEntity = countryDelegate.getCountryByCode(COUNTRY_ISO2_CODE);

        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryRepository).findByShortName2(COUNTRY_ISO2_CODE);
        verify(mockCountryNameClient, never()).getCountryByIso3Code(anyString());
        verify(mockCountryNameClient).getCountryByIso2Code(COUNTRY_ISO2_CODE);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockCountryTransformer).transform(groupKTCountry);
        verify(mockCountryRepository).save(country);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForLocation(country);
    }

    @Test
    public void shouldGetCountryWithIso2CodeFromDatabaseWhenHaveMatch() {
        when(mockCountryRepository.findByShortName2(COUNTRY_ISO2_CODE)).thenReturn(country);

        ResponseEntity responseEntity = countryDelegate.getCountryByCode(COUNTRY_ISO2_CODE);

        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryRepository).findByShortName2(COUNTRY_ISO2_CODE);
        verify(mockCountryNameClient, never()).getCountryByIso3Code(anyString());
        verify(mockCountryNameClient, never()).getCountryByIso2Code(COUNTRY_ISO2_CODE);
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockCountryTransformer, never()).transform(groupKTCountry);
        verify(mockCountryRepository, never()).save(country);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForLocation(country);
    }

    @Test
    public void shouldGetCountryWithIso3CodeFromClientWhenDatabaseNoHaveMatchAndAfterCallDatabaseForSaveTheResult() {
        when(mockCountryRepository.findByShortName3(COUNTRY_ISO3_CODE)).thenReturn(null);
        when(mockCountryNameClient.getCountryByIso3Code(COUNTRY_ISO3_CODE)).thenReturn(groupKTResponse);

        ResponseEntity responseEntity = countryDelegate.getCountryByCode(COUNTRY_ISO3_CODE);

        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryRepository).findByShortName3(COUNTRY_ISO3_CODE);
        verify(mockCountryNameClient, never()).getCountryByIso2Code(anyString());
        verify(mockCountryNameClient).getCountryByIso3Code(COUNTRY_ISO3_CODE);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(times(1));
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockCountryTransformer).transform(groupKTCountry);
        verify(mockCountryRepository).save(country);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForLocation(country);
    }

    @Test
    public void shouldGetCountryWithIso3CodeFromDatabaseWhenHaveMatch() {
        when(mockCountryRepository.findByShortName3(COUNTRY_ISO3_CODE)).thenReturn(country);

        ResponseEntity responseEntity = countryDelegate.getCountryByCode(COUNTRY_ISO3_CODE);

        assertThat(responseEntity, notNullValue());
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryRepository).findByShortName3(COUNTRY_ISO3_CODE);
        verify(mockCountryNameClient, never()).getCountryByIso2Code(anyString());
        verify(mockCountryNameClient, never()).getCountryByIso3Code(COUNTRY_ISO3_CODE);
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getRestResponse(groupKTResponse);
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getResult(groupKTRestResponse);
        verify(mockCountryTransformer, never()).transform(groupKTCountry);
        verify(mockCountryRepository, never()).save(country);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityForLocation(country);
    }

    @Test
    public void shouldGetCountryCallingClientsWithIsoCodesOnUppercase() {
        countryDelegate.getCountryByCode(COUNTRY_ISO3_CODE.toLowerCase());

        verify(mockCountryNameClient).getCountryByIso3Code(COUNTRY_ISO3_CODE);

        countryDelegate.getCountryByCode(COUNTRY_ISO2_CODE.toLowerCase());

        verify(mockCountryNameClient).getCountryByIso2Code(COUNTRY_ISO2_CODE);
    }

    @Test
    public void shouldGetMessageWhenIsNotTheAcceptedIsoCode() {
        when(ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_TRY_WITH_ISO_2_CODE_OR_ISO_3_CODE), HttpStatus.BAD_REQUEST)).
                thenReturn(ResponseEntity.badRequest().build());

        ResponseEntity responseEntity = countryDelegate.getCountryByCode(COUNTRY_NAME);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        verify(mockCountryNameClient, never()).getCountryByIso2Code(anyString());
        verify(mockCountryNameClient, never()).getCountryByIso3Code(anyString());
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getRestResponse(ArgumentMatchers.any());
        PowerMockito.verifyStatic(never());
        GroupKTHelper.getResult(ArgumentMatchers.any());
        verify(mockCountryTransformer, never()).transform(ArgumentMatchers.any());
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_TRY_WITH_ISO_2_CODE_OR_ISO_3_CODE), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void shouldSaveNewCountryWhenNotExistMatchOnDatabase() {
        when(mockCountryRepository.findByShortName3(COUNTRY_ISO3_CODE)).thenReturn(null);
        when(ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_STATE_SAVED), HttpStatus.OK)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = countryDelegate.saveCountry(country);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryRepository).findByShortName3(COUNTRY_ISO3_CODE);
        verify(mockCountryRepository).save(country);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_STATE_SAVED), HttpStatus.OK);
    }

    @Test
    public void shouldNotSaveNewCountryWhenExistMatchOnDatabase() {
        when(mockCountryRepository.findByShortName3(COUNTRY_ISO3_CODE)).thenReturn(country);
        when(ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_STATE_ALREADY_SAVE), HttpStatus.OK)).thenReturn(responseEntitySuccessful);

        ResponseEntity responseEntity = countryDelegate.saveCountry(country);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        verify(mockCountryRepository).findByShortName3(COUNTRY_ISO3_CODE);
        verify(mockCountryRepository, never()).save(country);
        PowerMockito.verifyStatic(times(1));
        ResponseEntityHelper.prepareResponseEntityMessage(Message.makeMessage(MESSAGE_VALUE_STATE_ALREADY_SAVE), HttpStatus.OK);
    }
}