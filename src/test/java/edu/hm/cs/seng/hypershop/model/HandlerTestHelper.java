package edu.hm.cs.seng.hypershop.model;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.attributes.persistence.PersistenceAdapter;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.response.ResponseBuilder;
import org.mockito.Mockito;

public class HandlerTestHelper {


    public static void buildInput(final RequestEnvelope requestEnvelope, final HandlerInput input){
        Mockito.when(input.getRequestEnvelope()).thenReturn(requestEnvelope);
        AttributesManager.Builder b = AttributesManager.builder();

        PersistenceAdapter pa = Mockito.mock(PersistenceAdapter.class);
        AttributesManager am = b.withPersistenceAdapter(pa).withRequestEnvelope(requestEnvelope).build();
        Mockito.when(input.getAttributesManager()).thenReturn(am);
        Mockito.when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
    }


}
