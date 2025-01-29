package com.appviewx.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;



@Component
@Scope("prototype")
public class TestSingletonWithConstructorParam {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestSingletonWithConstructorParam.class);

    int sequenceId;

    //public TestSingletonWithConstructorParam(){}

    public TestSingletonWithConstructorParam(int sequenceId) {
        this.sequenceId = sequenceId;
        LOGGER.info("sequenceId changed to : {}", this.sequenceId);
        LOGGER.info("this : {}", this);
    }

    public void printSequenceId() {
        LOGGER.info("sequenceId in printSequenceId : {}", this.sequenceId);
        LOGGER.info("this : {}", this);
    }

}
