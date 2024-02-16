package com.practice.design.pattern.command.impl.executor;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.common.exception.BadRequestException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CommandExecutor implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  public <R, T> Mono<T> execute(Class<? extends Command<R, T>> commandClass, R commandRequest) {
    return Mono.just(applicationContext.getBean(commandClass))
        .flatMap(command -> validate(command, commandRequest))
        .flatMap(command -> execute(command, commandRequest));
  }

  private <R, T> Mono<? extends T> execute(Command<R, T> command, R commandRequest) {
    return command.execute(commandRequest)
        .onErrorResume(throwable -> command.fallback(throwable, commandRequest));
  }

  private <R, T> Mono<Command<R,T>> validate(Command<R,T> command, R commandRequest) {
    if(command.validate(commandRequest)) {
      return Mono.just(command);
    }

    return Mono.error(new BadRequestException("invalid command request"));
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
