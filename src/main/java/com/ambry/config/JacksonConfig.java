package com.ambry.config;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ambry.progeny.deserializer.ClausDeserializer;
import com.ambry.progeny.deserializer.GailRiskDeserializer;
import com.ambry.progeny.deserializer.PremmDeserializer;
import com.ambry.progeny.deserializer.TyrerDeserializer;
import com.ambry.progeny.deserializer.BRCADeserializer;
import com.ambry.progeny.deserializer.MMRDeserializer;
import com.ambry.progeny.deserializer.MelaDeserializer;
import com.ambry.progeny.deserializer.PancDeserializer;
import com.ambry.progeny.deserializer.BRCADeserializer;
import com.ambry.progeny.serializer.ClausResultSerializer;
import com.ambry.progeny.serializer.GailResultSerializer;
import com.ambry.progeny.serializer.PredictionSerializer;
import com.ambry.progeny.serializer.PremmResultSerializer;
import com.ambry.progeny.serializer.TyrerResultSerializer;
import com.ambry.progeny.serializer.BrcaResultSerializer;
import com.ambry.progeny.serializer.MMRResultSerializer;
import com.ambry.progeny.serializer.MelaResultSerializer;
import com.ambry.progeny.serializer.PancResultSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.objex.risk.beans.BaseResult;
import com.objex.risk.beans.Prediction;
import com.objex.risk.beans.claus.ClausResult;
import com.objex.risk.beans.gail.GailResult;
import com.objex.risk.beans.gail.Risk;
import com.objex.risk.beans.mmr.MMRResult;
import com.objex.risk.beans.premm.PREMMResult;
import com.objex.risk.beans.tcuzick.TyrerResult;
import com.objex.risk.beans.brca.BRCAResult;
import com.objex.risk.beans.mela.MelaResult;
import com.objex.risk.beans.panc.PancResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Configuration
public class JacksonConfig {

    /**
     * Support for Java date and time API.
     * 
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
        //module.addSerializer(GailResult.class, new GailResultSerializer());
        //module.addSerializer(Prediction.class, new PredictionSerializer());
        //module.addSerializer(ClausResult.class, new ClausResultSerializer());
        //module.addSerializer(PREMMResult.class, new PremmResultSerializer());
        //module.addSerializer(TyrerResult.class, new TyrerResultSerializer());
        //module.addSerializer(BRCAResult.class, new BrcaResultSerializer());
        //module.addSerializer(MMRResult.class, new MMRResultSerializer());
        //module.addSerializer(MelaResult.class, new MelaResultSerializer());
        //module.addSerializer(PancResult.class, new PancResultSerializer());

        //module.addDeserializer(Risk.class, new GailRiskDeserializer());
        //module.addDeserializer(com.objex.risk.beans.claus.Risk.class, new ClausDeserializer());
        //module.addDeserializer(com.objex.risk.beans.premm.Risk.class, new PremmDeserializer());
        //module.addDeserializer(com.objex.risk.beans.tcuzick.Risk.class, new TyrerDeserializer());
        //module.addDeserializer(com.objex.risk.beans.brca.Risk.class, new BRCADeserializer());
        //module.addDeserializer(com.objex.risk.beans.mmr.Risk.class, new MMRDeserializer());
        //module.addDeserializer(com.objex.risk.beans.mela.Risk.class, new MelaDeserializer());
        //module.addDeserializer(com.objex.risk.beans.panc.Risk.class, new PancDeserializer());
        return module;
    }

    @Bean
    public ObjectMapper objectMapper(SimpleModule customSerializer) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.addMixIn(BaseResult.class, BaseResultMixIn.class);
        objectMapper.registerModule(customSerializer);
        return objectMapper;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.featuresToEnable(SerializationFeature.INDENT_OUTPUT); // Optional: Pretty-print XML
            builder.modulesToInstall(new JaxbAnnotationModule()); // Optional: Support JAXB annotations
        };
    }

}

abstract class BaseResultMixIn {
    @JsonIgnore
    public abstract byte[] getChart();
}
