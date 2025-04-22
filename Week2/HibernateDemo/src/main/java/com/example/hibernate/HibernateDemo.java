package com.example.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateDemo {
    public static void main(String[] args) {
        // Create configuration
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");

        // Build session factory
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        // ------------------------------------------------------------

        // 1. Transient state
        Employee emp = new Employee(); // Transient
        emp.setName("John");
        emp.setSalary(5000);

        // 2. Persistent state
        session.persist(emp); // Now persistent

        emp.setSalary(5500); // Hibernate tracks this change

        tx.commit(); // Changes are flushed to DB

        // 3. Detached state
        session.close(); // Now emp is in Detached state
        emp.setSalary(6000); // Change is not tracked

        // 4. Reattached state
        Session session2 = sessionFactory.openSession();
        Transaction tx2 = session2.beginTransaction();

        // Reattached → the managedEmp becomes persistent, **emp is still in Detached
        // state**
        Employee managedEmp = session2.merge(emp);
        tx2.commit();

        // 5. Removed state
        Transaction tx3 = session2.beginTransaction();

        // This will throw an exception because emp is in Detached state
        // session2.remove(emp);

        session2.remove(managedEmp); // Now scheduled for deletion (removed)
        tx3.commit();

        session2.close();
        sessionFactory.close();

        // try {
        // Transaction tx = session.beginTransaction();

        // Employee emp = new Employee();
        // emp.setName("Jessica");
        // emp.setSalary(6000);

        // Employee emp2 = new Employee();
        // emp2.setName("Peter");
        // emp2.setSalary(7000);

        // Department dept = new Department();
        // dept.setName("IT");

        // // Save department first
        // session.persist(dept);

        // emp.setDepartment(dept);
        // emp2.setDepartment(dept);

        // session.persist(emp);
        // session.persist(emp2);

        // Department dept2 = session.get(Department.class, 1);
        // session.remove(dept2);

        // tx.commit();
        // } catch (Exception e) {
        // System.out.println("Exception: " + e.getMessage());
        // } finally {
        // session.close();
        // sessionFactory.close();
        // }
    }
}