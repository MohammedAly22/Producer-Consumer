package boundedbufferproblem;

import java.util.List;
import java.util.Random;

public class Producer implements Runnable{
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    List<Integer> sharedList = null;
    final int  MAX_SIZE;
    Random random = new Random();
    int number = 0;
    
    public Producer(List<Integer> sharedList, int MAX_SIZE){
        this.sharedList = sharedList;
        this.MAX_SIZE = MAX_SIZE;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                produce(++number);
            } catch (InterruptedException ex) {}
        }
    }
    
    public void produce(int i) throws InterruptedException{
        synchronized(sharedList){
            while(sharedList.size() == MAX_SIZE){
                System.out.println(ANSI_YELLOW + Thread.currentThread().getName() + " -> Shared List is Full!" + ANSI_RESET);
                System.out.println("==================");
                sharedList.wait();
            }
            
            // producing an element
            Thread.sleep(random.nextInt(100, 3000));
            System.out.println(Thread.currentThread().getName() + " -> Is Producing an item: " + i);
            sharedList.add(i);
            System.out.println("Shared List: " + sharedList);
            System.out.println("==================");
            
            // update GUI
            page.sharedBuffer.setText(String.valueOf(sharedList));
            int producedCount = Integer.parseInt(page.producedCount.getText());
            page.producedCount.setText(String.valueOf(++producedCount));
            
            int consumedCount = Integer.parseInt(page.consumedCount.getText());
            page.remainingCount.setText(String.valueOf(producedCount - consumedCount));
            
            sharedList.notify();
        }
    }
}
