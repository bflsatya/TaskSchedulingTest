package com.appviewx.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SingletonBeanProvider {
    private ObjectProvider<TestSingletonWithConstructorParam> singletonProvider;

    @Autowired
    public SingletonBeanProvider(ObjectProvider<TestSingletonWithConstructorParam> singletonProvider) {
        this.singletonProvider = singletonProvider;
    }

    public TestSingletonWithConstructorParam getTestSingletonWithConstructorParam(int sequenceId) {
        return singletonProvider.getObject(sequenceId);
    }

}
