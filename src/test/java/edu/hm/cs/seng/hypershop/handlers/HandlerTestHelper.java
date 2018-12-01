package edu.hm.cs.seng.hypershop.handlers;

import com.amazon.ask.attributes.AttributesManager;
import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.model.RequestEnvelope;
import com.amazon.ask.response.ResponseBuilder;
import edu.hm.cs.seng.hypershop.util.PersistenceAdapterMockImpl;
import org.mockito.Mockito;

import java.util.HashMap;

public class HandlerTestHelper {


    public static void buildInput(final RequestEnvelope requestEnvelope, final HandlerInput input){
        Mockito.when(input.getRequestEnvelope()).thenReturn(requestEnvelope);
        final AttributesManager.Builder b = AttributesManager.builder();

        final AttributesManager am = b.withPersistenceAdapter(new PersistenceAdapterMockImpl()).withRequestEnvelope(requestEnvelope).build();
        am.setSessionAttributes(new HashMap<>());
        Mockito.when(input.getAttributesManager()).thenReturn(am);
        Mockito.when(input.getResponseBuilder()).thenReturn(new ResponseBuilder());
        Mockito.when(input.matches(Mockito.any())).thenCallRealMethod();
    }


}
