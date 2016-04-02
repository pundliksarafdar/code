package com.classapp.db;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	HibernateInit init = new HibernateInit();
    	init.createTablesIfNotExist();
    	
    }
}