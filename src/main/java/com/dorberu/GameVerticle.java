package com.dorberu;

import io.vertx.core.AbstractVerticle;

public class GameVerticle extends AbstractVerticle {

  @Override
  public void start() {
	  System.out.println(getClass().getName() + " DEBUG " + "start");
  }
}