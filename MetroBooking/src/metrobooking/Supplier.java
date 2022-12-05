package metrobooking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import metrobooking.Ticket;

public class Supplier implements Runnable{
    final int MAX_SIZE;
    List<Ticket> ticketsWindow;
    Random r = new Random();
    int ticketID = 0;
    double[] ticketPrices = {5, 7, 10};
    
    public Supplier(List<Ticket> tikcetsWindow, int MAX_SIZE){
        this.MAX_SIZE = MAX_SIZE;
        this.ticketsWindow = tikcetsWindow;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                produceTicket(++ticketID);
            } catch (InterruptedException ex) {}
        }
    }
    
    public void produceTicket(int i) throws InterruptedException{
        synchronized(ticketsWindow){
            while(ticketsWindow.size() == MAX_SIZE){
                System.out.println(Thread.currentThread().getName() + " -> Tickets Window now is FULL!");
                System.out.println("=============");
                ticketsWindow.wait();
            }
            
            // create new ticket
            Ticket ticket = new Ticket();
            ticket.setTicketID(i);
            ticket.setPrice(ticketPrices[ r.nextInt(0,2) ]);
            
            System.out.println(Thread.currentThread().getName() + " -> is Producing a ticket");
            ticket.printTicketInfo();
            
            // add ticket to tickets window
            Thread.sleep(r.nextInt(100, 3000));
            ticketsWindow.add(ticket);
            
            // create list of tickets IDs
            List<Integer> ticketsWindowIDs = new ArrayList<Integer>();
            
            // print tickets
            System.out.print("Tickets Window: ");
            for(int j = 0 ; j < ticketsWindow.size() ; j++){
                System.out.print("[" + ticketsWindow.get(j).ticketID + "]");
                ticketsWindowIDs.add(ticketsWindow.get(j).getTicketID());
            }
            System.out.println("\n=============");
            
            // update GUI
            page.ticketWindow.setText(String.valueOf(ticketsWindowIDs));
            int producedCount = Integer.parseInt(page.producedCount.getText());
            page.producedCount.setText(String.valueOf(++producedCount));
            
            int boughtCount = Integer.parseInt(page.boughtCount.getText());
            page.remainingCount.setText(String.valueOf(producedCount - boughtCount));
            
            ticketsWindow.notifyAll();
        }
    }
}
