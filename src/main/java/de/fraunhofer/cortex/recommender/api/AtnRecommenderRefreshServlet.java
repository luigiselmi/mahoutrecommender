/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.fraunhofer.cortex.recommender.api;

import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.model.Preference;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.model.UpdatableIDMigrator;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fraunhofer.cortex.recommender.cf.AtnItemBasedRecommender;
import de.fraunhofer.cortex.recommender.cf.AtnUserBasedRecommender;
import de.fraunhofer.cortex.recommender.model.SignalsDataModel;


/**
 * <p>A servlet which loads the signals and updates the recommendations. The servlet accepts GET and POST
 * HTTP requests</p>
 *
 */
public final class AtnRecommenderRefreshServlet extends HttpServlet {

  static final Logger LOG = LoggerFactory.getLogger(AtnRecommenderRefreshServlet.class);

  private AtnItemBasedRecommender recommender;
  private SignalsDataModel model; 

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    String recommenderClassName = config.getInitParameter("recommender-class");
    if (recommenderClassName == null) {
      throw new ServletException("Servlet init-param \"recommender-class\" is not defined");
    }
    try {
      RecommenderSingleton.initializeIfNeeded(recommenderClassName);
    } catch (TasteException te) {
      throw new ServletException(te);
    }
    
    recommender = (AtnItemBasedRecommender) RecommenderSingleton.getInstance().getRecommender();
    model = (SignalsDataModel) recommender.getDataModel();
  
  }

  @Override
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException {

    LOG.info("Starting reading new signal files and updating recommendations");   
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-cache");
    PrintWriter writer;
	try {
		int currentItems = recommender.getDataModel().getNumItems();
	    int currentUsers = recommender.getDataModel().getNumUsers();
		recommender.getDataModel().refresh(null);
	    int updatedItems = recommender.getDataModel().getNumItems();
	    int updatedUsers = recommender.getDataModel().getNumUsers();
	    int deltaItems = updatedItems - currentItems;
	    int deltaUsers = updatedUsers - currentUsers;
	    recommender.refresh(null);
		writer = response.getWriter();
		writer.println("Recommendations updated. Num. items: " + updatedItems + ", Num. users: " + updatedUsers);
		writer.close();
		LOG.info("Users: " + updatedUsers + 
				" (diff. " + deltaUsers + "). \n Items: " + 
				updatedItems + " (diff. " + deltaItems + ").");
	} 
	catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    catch (TasteException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	
	LOG.info("Finished updating recommendations");
 
  }
  
  
  @Override
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException {
    doGet(request, response);
  }

  @Override
  public String toString() {
    return "AtnRecommenderRefreshServlet[recommender:" + recommender + ']';
  }

}
