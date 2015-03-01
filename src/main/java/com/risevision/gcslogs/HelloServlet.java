package com.risevision.gcslogs;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.*;
import javax.servlet.ServletException;

public class HelloServlet extends HttpServlet {
  private static final Logger log = Logger.getAnonymousLogger();

  public void doGet (HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException  {
    log.info("HelloServlet: hello");
  }
}

