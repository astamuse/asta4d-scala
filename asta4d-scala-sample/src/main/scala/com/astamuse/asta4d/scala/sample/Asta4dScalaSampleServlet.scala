package com.astamuse.asta4d.scala.sample

import com.astamuse.asta4d.web.servlet.Asta4dServlet
import com.astamuse.asta4d.web.WebApplicationConfiguration

class Asta4dScalaSampleServlet extends Asta4dServlet{
  override def createConfiguration(): WebApplicationConfiguration = {
    val conf = super.createConfiguration();
    val debug = java.lang.Boolean.getBoolean("asta4d-scala-sample.debug");
    if (debug) {
        conf.setCacheEnable(false);
        conf.setSaveCallstackInfoOnRendererCreation(true);
    }
    return conf;
  }
}