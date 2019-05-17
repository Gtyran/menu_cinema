/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package l3m.Able;

/**
 *
 * @author hanh1
 */
public interface DataBaseAble {
    
    public void connectToDataBase();
    
    public void disconnect();
    
    public Object request(String request);
    
    public void logs(String request);
}
