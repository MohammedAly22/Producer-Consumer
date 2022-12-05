package boundedbufferproblem;

import java.util.List;
import java.util.Random;

public class Consumer implements Runnable{
    List<Integer> sharedList = null;
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    Random random = new Random();
    
    public Consumer(List<Integer> sharedList){
        this.sharedList = sharedList;
    }
    
    @Override
    public void run(){
        while(true){
            try {
                consume();
            } catch (InterruptedException ex) {}
        }
    }
    
    public void consume() throws InterruptedException{
        synchronized(sharedList){
            while(sharedList.isEmpty()){
                System.out.println(ANSI_YELLOW + Thread.currentThread().getName() + " -> Shared List is Empty!" + ANSI_RESET);
                System.out.println("==================");
                sharedList.wait();
            }
            
            // consuming first element
            Thread.sleep(random.nextInt(100, 3000));
            int number = sharedList.remove(0);
            System.out.println(Thread.currentThread().getName() + " -> Is Consuming an item: " + number);
            System.out.println("Shared List: " + sharedList);
            System.out.println("==================");
            
            // update GUI
            page.sharedBuffer.setText(String.valueOf(sharedList));
            int consumedCount = Integer.parseInt(page.consumedCount.getText());
            page.consumedCount.setText(String.valueOf(++consumedCount));
            int producedCount = Integer.parseInt(page.producedCount.getText());
            page.remainingCount.setText(String.valueOf(producedCount - consumedCount));
            
            sharedList.notifyAll();
        }
    }
}
