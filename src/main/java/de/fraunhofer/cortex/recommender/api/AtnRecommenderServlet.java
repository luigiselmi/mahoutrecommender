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
 * <p>A servlet which returns recommendations, as its name implies. The servlet accepts GET and POST
 * HTTP requests, and looks for two parameters:</p>
 *
 * <ul>
 * <li><em>userID</em>: the user ID for which to produce recommendations</li>
 * <li><em>howMany</em>: the number of recommendations to produce</li>
 * 
 * <p>For example, you can get 10 recommendations for user 123 from the following URL (assuming
 * you are running taste in a web application running locally on port 8080):<br/>
 * {@code http://localhost:8080/taste/RecommenderServlet?userID=123&howMany=10}</p>
 *
 * <p>This servlet requires one {@code init-param} in {@code web.xml}: it must find
 * a parameter named "recommender-class" which is the name of a class that implements
 * {@link Recommender} and has a no-arg constructor. The servlet will instantiate and use
 * this {@link Recommender} to produce recommendations.</p>
 */
public final class AtnRecommenderServlet extends HttpServlet {

  static final Logger LOG = LoggerFactory.getLogger(AtnRecommenderServlet.class);
  private static final int NUM_TOP_PREFERENCES = 20;
  private static final int DEFAULT_HOW_MANY = 20;

  //private AtnUserBasedRecommender recommender;
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
    
    //recommender = (AtnUserBasedRecommender) RecommenderSingleton.getInstance().getRecommender();
    recommender = (AtnItemBasedRecommender) RecommenderSingleton.getInstance().getRecommender();
    model = (SignalsDataModel) recommender.getDataModel();
  
  }

  @Override
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException {

    String userIDString = request.getParameter("userID");
    if (userIDString == null) {
      throw new ServletException("userID was not specified");
    }
    long userID = Long.parseLong(userIDString);
    String howManyString = request.getParameter("howMany");
    int howMany = howManyString == null ? DEFAULT_HOW_MANY : Integer.parseInt(howManyString);
    
    LOG.info("userID = " + userID + ", howMany = " + howMany);
    try {
      List<RecommendedItem> items = recommender.recommend(userID, howMany);
      writeJSON(userID, response, items);
 
    } 
    catch (NoSuchUserException nsue) {
      try {
        writeJSON(userID, response, null);
      } 
      catch (IOException | TasteException e1) {  
        e1.printStackTrace();
      }
    }
    catch (TasteException te) {
      throw new ServletException(te);
    } 
    catch (IOException ioe) {
      throw new ServletException(ioe);
    }

  }
  
  private void writeJSON(long userID, HttpServletResponse response, Iterable<RecommendedItem> items) throws IOException, TasteException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-cache");
    PrintWriter writer = response.getWriter();
    writer.println(jsonRecommendedItem(userID, items));
  }
  /**
   * Serialize the list of recommended items in json 
   * @param items
   * @return
   * @throws TasteException 
   */
  private String jsonRecommendedItem(long userID, Iterable<RecommendedItem> items) throws TasteException {
  	JsonArrayBuilder jrecommendations = Json.createArrayBuilder();
  	if(items != null) {
  	  for(RecommendedItem ri: items){
  		  JsonObject jrecommendation = Json.createObjectBuilder()
  	    	.add("itemID", model.getItemIDAsString(ri.getItemID()))
  	    	.add("value", ri.getValue())
  	    	.build();
  		  jrecommendations.add(jrecommendation);	  	
  	  }
  	}
  	JsonObject jrecommendedItems = Json.createObjectBuilder()
  	    .add("userID", userID)
  			.add("recommendedItems", jrecommendations).build();    
      return jrecommendedItems.toString();
  }

  @Override
  public void doPost(HttpServletRequest request,
                     HttpServletResponse response) throws ServletException {
    doGet(request, response);
  }

  @Override
  public String toString() {
    return "RecommenderServlet[recommender:" + recommender + ']';
  }

}
