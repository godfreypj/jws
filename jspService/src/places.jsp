<jsp:useBean id    = "plcs" 
	     type  = "places.Places" 
	     class = "places.Places"> 
  <% 
     String verb = request.getMethod();

     if (!verb.equalsIgnoreCase("GET")) {
       response.sendError(response.SC_METHOD_NOT_ALLOWED,
                          "GET requests only are allowed.");
     }
     // If it's a GET request, return the place.
     else {
       // Object reference application has the value 
       // pageContext.getServletContext()
       plcs.setServletContext(application);
       out.println(plcs.getPlaces());
     }
  %>
</jsp:useBean>  
