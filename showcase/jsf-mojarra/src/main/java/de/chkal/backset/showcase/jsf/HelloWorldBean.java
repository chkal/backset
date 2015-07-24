package de.chkal.backset.showcase.jsf;

import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean
@RequestScoped
public class HelloWorldBean {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldBean.class);

    public HelloWorldBean() {
        log.info("ctor");
    }

    private String param;

    public String getParam() {
        log.info("getParam => " + param);
        return param;
    }

    public void setParam(String param) {
        log.info("setParam := " + param);
        this.param = param;
    }

    public String getDate() {
        log.info("getDate");
        return new Date().toString();
    }

}
