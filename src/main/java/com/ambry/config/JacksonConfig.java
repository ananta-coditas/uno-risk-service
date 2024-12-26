package com.ambry.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ambry.progeny.deserializer.ClausDeserializer;
import com.ambry.progeny.deserializer.GailRiskDeserializer;
import com.ambry.progeny.deserializer.PremmDeserializer;
import com.ambry.progeny.deserializer.TyrerDeserializer;
import com.ambry.progeny.serializer.ClausResultSerializer;
import com.ambry.progeny.serializer.GailResultSerializer;
import com.ambry.progeny.serializer.PredictionSerializer;
import com.ambry.progeny.serializer.PremmResultSerializer;
import com.ambry.progeny.serializer.TyrerResultSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.gail.Risk;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.tcuzick.TyrerResult;

@Configuration
public class JacksonConfig {

    /**
     * Support for Java date and time API.
     * @return the corresponding Jackson module.
     */
    @Bean
    public JavaTimeModule javaTimeModule() {
        return new JavaTimeModule();
    }

    @Bean
    public Jdk8Module jdk8TimeModule() {
        return new Jdk8Module();
    }
    
    @Bean
    public SimpleModule customSerializer() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(GailResult.class, new GailResultSerializer());
        module.addSerializer(Prediction.class, new PredictionSerializer());
        module.addSerializer(ClausResult.class, new ClausResultSerializer());
        module.addSerializer(PREMMResult.class, new PremmResultSerializer());
        module.addSerializer(TyrerResult.class, new TyrerResultSerializer());
        
        module.addDeserializer(Risk.class, new GailRiskDeserializer()); 
        module.addDeserializer(com.objex.risk.beans.claus.Risk.class, new ClausDeserializer());
        module.addDeserializer(com.objex.risk.beans.premm.Risk.class, new PremmDeserializer());
        module.addDeserializer(com.objex.risk.beans.tcuzick.Risk.class, new TyrerDeserializer());
        return module;
    }
    
    @Bean
    public ObjectMapper objectMapper(SimpleModule customSerializer) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(customSerializer);  
        return objectMapper;
    }
}
