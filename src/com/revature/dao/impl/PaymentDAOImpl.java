package com.revature.dao.impl;

import java.sql.*;

import com.revature.dao.PaymentDAO;
import com.revature.util.DBConnection;

public class PaymentDAOImpl implements PaymentDAO{
	
	@Override
    public boolean makePayment(int orderId, String method){

        String insertPayment =
            "INSERT INTO payments(payment_id, order_id, payment_method, payment_status) " +
            "VALUES(payments_seq.NEXTVAL, ?, ?, 'SUCCESS')";

        String updateOrder =
            "UPDATE orders SET order_status='PAID' WHERE order_id=?";

        try(Connection con = DBConnection.getConnection()){

            con.setAutoCommit(false);

            PreparedStatement ps =
                con.prepareStatement(insertPayment);

            ps.setInt(1, orderId);
            ps.setString(2, method);

            ps.executeUpdate();

            PreparedStatement orderPs =
                con.prepareStatement(updateOrder);

            orderPs.setInt(1, orderId);
            orderPs.executeUpdate();

            con.commit();

            return true;

        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

}
