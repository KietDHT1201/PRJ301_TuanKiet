/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kiet.controller;

import kiet.registration.RegistrationDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import kiet.registration.RegistrationDTO;
import kiet.util.MyAppConstants;

/**
 *
 * @author ASUS
 */
public class LoginServlet extends HttpServlet {
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String button = request.getParameter("btAction");
        //1. Get context Scope 
        ServletContext context = this.getServletContext();
        //2. Get SITEMAPS
        Properties siteMaps = (Properties) context.getAttribute("SITEMAPS");
        
        String url = siteMaps.getProperty(MyAppConstants.LoginFeatures.INVALID_PAGE);
        
        try{
            //check value
            if (button.equals("Login")) {
                //đúng button mới lấy được parameter
                String username = request.getParameter("txtUsername");
                String password = request.getParameter("txtPassword");
                
                //1.call DAO
                //new DAO & call method of DAO
                RegistrationDAO dao = new RegistrationDAO();
                RegistrationDTO result = dao.checkLogin(username, password);
                //2.process result
                if (result != null) {
                    url = siteMaps.getProperty(MyAppConstants.LoginFeatures.SEARCH_ACTION);
                    
                    HttpSession session = request.getSession();
                    session.setAttribute("USER", result);
//                    Cookie cookie = new Cookie(username, password);
//                    cookie.setMaxAge(60 * 5);
//                    response.addCookie(cookie);// ép cookie vào trong respone obj
                }// end user had existed
            }// end if user clicked Loign
        } catch (SQLException ex){
            log("LoginServlet _ SQL " + ex.getMessage());
        } catch(NamingException ex){
            log("LoginServlet _ Naming " + ex.getMessage());
        }
        finally{
            response.sendRedirect(url);
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
