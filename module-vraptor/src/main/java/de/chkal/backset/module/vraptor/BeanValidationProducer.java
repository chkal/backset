package de.chkal.backset.module.vraptor;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@ApplicationScoped
public class BeanValidationProducer {

  private ValidatorFactory validatorFactory;

  @PostConstruct
  public void init() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
  }

  @Produces
  @Dependent
  public ValidatorFactory produceValidatorFactory() {
    return validatorFactory;
  }

  @Produces
  @Dependent
  public Validator produceValidator() {
    return validatorFactory.getValidator();
  }

}
