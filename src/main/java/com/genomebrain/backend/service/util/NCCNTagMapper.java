package com.genomebrain.backend.service.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.genomebrain.backend.quesstionnaire.QuestionSet;

public enum NCCNTagMapper {
    Sex_at_birth("What was your sex at birth","AMBRY002","male","AMBRY002_001");

    private final String[] values;

    NCCNTagMapper(String... values) {
        this.values = values;
    }

    public static int QuestionAndAnswerMapper(String questionEnum, String answerValue) {

        // System.out.println(Program.);
        for (int i = 0; i < values().length; i++) {
            List list= Arrays.stream(values()[i].values).collect(Collectors.toList());
            if(list.contains("What was your sex at birth")){
                int n=list.indexOf("What was your sex at birth");
                QuestionSet questionSet = new QuestionSet();
                questionSet.setQuestionText((String) list.get(n));
                questionSet.setQuestionKey((String) list.get(n+1));
                questionSet.setAnswerText((String) list.get(n));
                questionSet.setAnswerKey((String) list.get(n));
                System.out.println(list.get(n+1));}

        }
        return -1; // Code not found
    }

}
