package de.do9fse.winkey.lib.core.model.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.do9fse.winkey.lib.core.model.ProtocolVersion;
import de.do9fse.winkey.lib.core.model.responses.EmptyResponse;
import de.do9fse.winkey.lib.core.model.responses.Response;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ImmediateCommandConfiguration {
    ProtocolVersion[] allowedProtocols();
    Class<? extends Response> responseType() default EmptyResponse.class;
}